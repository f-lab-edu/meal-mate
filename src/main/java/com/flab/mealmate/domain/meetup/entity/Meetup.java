package com.flab.mealmate.domain.meetup.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import com.flab.mealmate.domain.model.BaseEntity;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "meetup")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meetup extends BaseEntity {

	@Id @Tsid
	private Long id;

	@Column(nullable = false)
	@Comment("제목")
	private String title;

	@Column(nullable = false)
	@Comment("내용")
	private String content;

	@Embedded
	private MeetupSchedule schedule;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Comment("진행 상태")
	private ProgressStatus progressStatus;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Comment("참여 유형")
	private ParticipationType participationType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Comment("참가자 모집 상태")
	private RecruitmentStatus recruitmentStatus;

	@Column(nullable = false)
	@Comment("최소 인원")
	private Integer minParticipants;

	@Column(nullable = false)
	@Comment("최대 인원")
	private Integer maxParticipants;

	@Column(name = "search_text", insertable = false, updatable = false)
	@Comment("검색용 필드")
	private String searchText;

	@OneToMany(mappedBy = "meetup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<MeetupParticipant> participants = new ArrayList<>();

	private Meetup(String title, String content, MeetupSchedule meetupSchedule, ParticipationType participationType,
		Integer minParticipants, Integer maxParticipants) {
		this.title = title;
		this.content = content;
		this.schedule = meetupSchedule;
		this.participationType = participationType;
		this.minParticipants = minParticipants;
		this.maxParticipants = maxParticipants;
		this.recruitmentStatus = RecruitmentStatus.OPEN;
		this.progressStatus = ProgressStatus.SCHEDULED;
		validateParticipants();
		addHostParticipant();
	}

	public static Meetup create(String title, String content, MeetupSchedule meetupSchedule, ParticipationType participationType,
		Integer minParticipants, Integer maxParticipants) {
		return new Meetup(title, content, meetupSchedule, participationType, minParticipants, maxParticipants);
	}

	void addAutoApprovedParticipant(Long userId) {
		validateDuplicateParticipant(userId);
		validateMaxParticipantsNotExceeded();
		this.participants.add(MeetupParticipant.createParticipant(this, ParticipationStatus.APPROVED));
	}

	public void addPendingParticipant(String applicationMessage, Long userId) {
		validateDuplicateParticipant(userId);
		this.participants.add(MeetupParticipant.createParticipant(this, ParticipationStatus.PENDING, applicationMessage));
	}

	protected void addHostParticipant() {
		this.participants.add(MeetupParticipant.createParticipant(this, ParticipationStatus.APPROVED));
	}

	private void validateParticipants() {
		if (this.minParticipants > this.maxParticipants) {
			throw new BusinessException(ErrorCode.ERR_MEETUP_002);
		}
	}

	protected void validateDuplicateParticipant(Long userId) {
		boolean hasActive = this.participants.stream()
			.anyMatch(p -> p.isActiveParticipant(userId));

		if (hasActive) {
			throw new BusinessException(ErrorCode.ERR_MEETUP_PARTICIPANT_001);
		}
	}
	protected void validateMaxParticipantsNotExceeded() {
		if (isOverMaxParticipants()) {
			throw new BusinessException(ErrorCode.ERR_MEETUP_PARTICIPANT_002);
		}
	}

	private boolean isOverMaxParticipants() {
		long approvedCount = this.participants.stream()
			.filter(MeetupParticipant::isApproved)
			.count();

		return approvedCount >= this.maxParticipants;
	}

}
