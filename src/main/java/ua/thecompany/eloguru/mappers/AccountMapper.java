package ua.thecompany.eloguru.mappers;

import org.mapstruct.*;


import ua.thecompany.eloguru.dto.AccountDto;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

//    Account accountDtoToAccountModel(AccountDto accountDto);

    AccountDto accountModelToAccountDto(Account account);

    Account accountInitDtoToAccountModel(AccountInitDto accountInitDto);

    Account updateAccountModelViaAccountInitDto(AccountInitDto accountInitDto, @MappingTarget Account account);
}
