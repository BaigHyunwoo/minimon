package com.minimon.repository;

import org.springframework.data.repository.CrudRepository;

import com.minimon.dto.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {

	Member findByUid(String id);

}
