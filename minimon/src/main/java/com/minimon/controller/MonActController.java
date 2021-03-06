package com.minimon.controller;

import com.google.common.net.HttpHeaders;
import com.minimon.common.CommonResponse;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonAct;
import com.minimon.entity.MonResult;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.exception.ActTestFileDownLoadException;
import com.minimon.service.MonActService;
import com.minimon.service.MonitoringService;
import com.minimon.vo.MonitoringResultVO;
import com.minimon.vo.MonitoringTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/monAct")
@Api(tags = {"Monitoring Act Controller"})
public class MonActController {


    private final MonActService monActService;
    private final MonitoringService monitoringService;


    @ApiOperation(value = "목록 조회", response = MonAct.class)
    @GetMapping(path = "")
    public CommonResponse getList(@ModelAttribute CommonSearchSpec commonSearchSpec) {
        return new CommonResponse(monActService.getList(commonSearchSpec));
    }

    @ApiOperation(value = "조회", response = MonAct.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        Optional act = monActService.get(seq);
        if (!act.isPresent()) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(act.get());
    }

    @ApiOperation(value = "생성", response = MonAct.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonAct monAct) {
        return new CommonResponse(monActService.save(monAct));
    }

    @ApiOperation(value = "수정", response = void.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonAct monAct) {
        if (!monActService.edit(monAct)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "삭제", response = void.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        if (!monActService.remove(seq)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "검사 테스트", produces = "multipart/form-data", response = MonitoringResultVO.class)
    @PostMapping(value = "/check")
    public CommonResponse check(@RequestParam MultipartFile actFile) {
        MonitoringResultVO monitoringResultVO = monActService.checkFile(actFile);
        if (monitoringResultVO == null) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(monitoringResultVO);
    }

    @ApiOperation(value = "검사 실행", response = MonResult.class)
    @GetMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        if (!monActService.get(seq).isPresent()) {
            return CommonResponse.notExistResponse();
        }

        monitoringService.addTask(MonitoringTaskVO.builder()
                .monitoringType(MonitoringTypeEnum.ACT)
                .seq(seq)
                .task(monActService.executeTask(seq))
                .build());
        return new CommonResponse();
    }

    @ApiOperation(value = "테스트 검사 파일 다운로드")
    @GetMapping(path = "/download/test")
    public ResponseEntity downloadTestFile() {
        try {
            File testFile = monActService.getTestFile();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + testFile.getName() + "\"")
                    .contentLength(testFile.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(new FileInputStream(testFile)));
        } catch (FileNotFoundException e) {
            throw new ActTestFileDownLoadException(e);
        }
    }
}