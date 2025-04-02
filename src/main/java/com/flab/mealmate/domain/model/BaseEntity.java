package com.flab.mealmate.domain.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

	@CreatedBy
	@AttributeOverrides({
		@AttributeOverride(name = "userId", column = @Column(name = "creator_id", updatable = false)),
		@AttributeOverride(name = "userName", column = @Column(name = "creator_name", updatable = false))
	})
	private User createdBy;

	@LastModifiedBy
	@AttributeOverrides({
		@AttributeOverride(name = "userId", column = @Column(name = "modifier_id")),
		@AttributeOverride(name = "userName", column = @Column(name = "modifier_name"))
	})
	private User modifiedBy;

	public boolean isCreatedIdEqualTo(Long createdId) {
		return this.createdBy.equalsUserId(createdId);
	}

}
