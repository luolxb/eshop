package com.soubao.dao;

import com.soubao.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsElasticsearchMapper extends ElasticsearchRepository<Goods, Long> {


}