package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.SpecImage;
import com.soubao.service.SpecImageService;
import com.soubao.dao.SpecImageMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
@Service("specImageService")
public class SpecImageServiceImpl extends ServiceImpl<SpecImageMapper, SpecImage> implements SpecImageService {

}
