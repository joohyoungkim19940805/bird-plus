package com.radcns.bird_plus.entity.account.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.account.vo.AccountVo;

/**
 * 
 * @author kim.joohyoung
 * BasicUserDTO dto = BasicMapper.INSTANCE.convert(user);
 */
@Mapper
public interface AccountMapper{
	AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

	AccountEntity entity(AccountVo vo);

	AccountVo vo(AccountEntity entity);
}