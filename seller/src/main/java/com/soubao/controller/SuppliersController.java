package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Seller;
import com.soubao.entity.Suppliers;
import com.soubao.service.SuppliersService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/suppliers")
public class SuppliersController {
    @Autowired
    private SuppliersService suppliersService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("page")
    public IPage<Suppliers> getPage(@RequestParam(value = "store_id", required = false) Integer storeId,
                                    @RequestParam(value = "suppliers_name", required = false) String suppliersName,
                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Suppliers> queryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            queryWrapper.eq("store_id", storeId);
        }
        if (StringUtils.isNotEmpty(suppliersName)) {
            queryWrapper.like("suppliers_name", suppliersName);
        }
        return suppliersService.page(new Page<>(page, size), queryWrapper);
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @ApiOperation("获取供应商列表")
    @GetMapping("list")
    public List<Suppliers> list() {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        return suppliersService.list(new QueryWrapper<Suppliers>().eq("store_id", seller.getStoreId()).eq("is_check",1));
    }

    @GetMapping
    public Suppliers getSuppliers(@RequestParam(value = "suppliers_id", required = false) Integer suppliersId) {
        return suppliersService.getById(suppliersId);
    }

    @PostMapping
    public SBApi saveSuppliers(@Valid @RequestBody Suppliers suppliers) {
        suppliersService.save(suppliers);
        return new SBApi();
    }

    @PutMapping
    public SBApi updateSuppliers(@Valid @RequestBody Suppliers suppliers) {
        suppliersService.updateById(suppliers);
        return new SBApi();
    }

    @DeleteMapping("{suppliers_id}")
    public SBApi deleteSuppliers(@PathVariable(value = "suppliers_id") Integer suppliersId) {
        suppliersService.removeById(suppliersId);
        return new SBApi();
    }
}
