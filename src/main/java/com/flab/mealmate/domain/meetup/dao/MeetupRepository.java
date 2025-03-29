package com.flab.mealmate.domain.meetup.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flab.mealmate.domain.meetup.entity.Meetup;

public interface MeetupRepository extends JpaRepository<Meetup, Long> {
}
