package com.flab.mealmate.domain.meetup.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flab.mealmate.domain.meetup.entity.MeetupParticipant;

public interface MeetupParticipantRepository extends JpaRepository<MeetupParticipant, Long> {
}
