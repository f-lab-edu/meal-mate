package com.flab.mealmate.domain.meetup.entity;

import org.hibernate.annotations.Comment;

import com.flab.mealmate.domain.model.BaseEntity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "meetup_participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetupParticipant extends BaseEntity {

	@Id @Tsid
	private Long id;

	@ManyToOne
	@JoinColumn(name = "meetup_id")
	private Meetup meetup;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Comment("참여자의 상태")
	private ParticipationStatus participationStatus;

	@Comment("참여 신청 시 작성하는 내용")
	private String applicationMessage;

	private MeetupParticipant(Meetup meetup, ParticipationStatus participationStatus, String applicationMessage) {
		this.meetup = meetup;
		this.participationStatus = participationStatus;
		this.applicationMessage = applicationMessage;
	}

	public static MeetupParticipant createHostParticipant(Meetup meetup) {
		return new MeetupParticipant(meetup, ParticipationStatus.APPROVED, null);
	}

	public static MeetupParticipant createPendingParticipant(Meetup meetup, String applicationMessage) {
		return new MeetupParticipant(meetup, ParticipationStatus.PENDING, applicationMessage);
	}

	public static MeetupParticipant createApprovedParticipant(Meetup meetup) {
		return new MeetupParticipant(meetup, ParticipationStatus.APPROVED, null);
	}
}
