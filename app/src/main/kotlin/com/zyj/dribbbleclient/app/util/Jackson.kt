package com.zyj.dribbbleclient.app.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

/**
 * Created by zhaoyuanjie on 15/10/13.
 */
public object Jackson {

    fun mapper() : ObjectMapper {
        val mapper = ObjectMapper().registerKotlinModule()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        return mapper
    }
}