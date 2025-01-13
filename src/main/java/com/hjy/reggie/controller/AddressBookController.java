package com.hjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hjy.reggie.common.BaseContext;
import com.hjy.reggie.common.R;
import com.hjy.reggie.entity.AddressBook;
import com.hjy.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook={}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    @PutMapping
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook={}", addressBook);
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(updateWrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }
}
