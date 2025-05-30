package com.axity.office.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;


/**
 * Dozer configuration class
 * 
 * @author username@axity.com
 */
@Configuration
@Component
public class DozerConfig
{
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
