package com.minimon.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.minimon.dto.Member;
import com.minimon.repository.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	MemberRepository memberRepository;
	

	@Override
	public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
		
		UserDetails user = null;
		
			
		Object member = memberRepository.findByUid(uid);
		
		if(member != null) {
			user = Optional.ofNullable(member)
					.filter(m -> m != null)
					.map(m -> new SecurityMember((Member) m)).get();
		}else {

			throw new UsernameNotFoundException("유저 X : " + uid);
			
		}
			
		
		return user;
				
	}
}
