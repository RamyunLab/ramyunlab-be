package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.repository.RamyunRepository;

import java.util.List;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private RamyunRepository ramyunRepository;

    public List<RamyunEntity> getGoodsList(){
        List<RamyunEntity> result = ramyunRepository.findAll();


    return result;
    }
}
