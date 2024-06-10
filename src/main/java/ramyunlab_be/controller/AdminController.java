package ramyunlab_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.service.AdminService;
import ramyunlab_be.vo.StatusCode;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/goods")
    public ResponseEntity<ResDTO> getGoodsList(){
//        List<RamyunDTO> result = AdminService.getGoodsList();
        return ResponseEntity.ok().body(ResDTO.builder().statusCode(StatusCode.OK).build());
    }
}
