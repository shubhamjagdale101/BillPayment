package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.model.UniqueID;
import com.shubham.lightbill.lightbill_backend.repository.IdGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService {
    @Autowired
    private IdGeneratorRepository idGeneratorRepository;

    public String generateId(String className, String prefix){
        UniqueID id = idGeneratorRepository.findByName(className);
        int value;

        if(id == null) {
            id = UniqueID.builder()
                    .name(className)
                    .counter(1)
                    .build();
            value = 0;
            idGeneratorRepository.save(id);
        }
        else {
            value = id.getCounter();
            id.setCounter(id.getCounter()+1);
            idGeneratorRepository.save(id);
        }

        String suffix = String.format("%06d", value);
        return prefix + suffix;
    }
}
