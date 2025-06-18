package org.jun.saemangeum.process.presentation;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.process.application.service.ContentDataProcessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/u3920abjd39a")
@RequiredArgsConstructor
public class AdminController {

    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    private final ContentDataProcessService contentDataProcessService;

    @GetMapping("/admin/process")
    public String collectProcessByAdmin(@RequestBody AdminDTO dto) {
        if (!(dto.username().equals(username) && dto.password().equals(password))) {
            return "잘못된 접근입니다";
        }

        contentDataProcessService.collectAndSaveAsync();
        return "데이터 수집 시작합니다";
    }
}
