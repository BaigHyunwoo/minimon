package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonCodeData;
import com.minimon.entity.MonTransaction;
import com.minimon.service.MonTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/monTransaction")
@Api(tags = {"Monitoring Transaction Controller"})
public class MonTransactionController {

    private final MonTransactionService monTransactionService;


    /**
     * TODO
     * 트랜잭션 기능 selenium plugin 이용 -> test.java 파일 업로드 후 compile하여 테스트 실행 및 결과 받아 올 수 있도록 변경
     * 불가능 시 현재 최신 selenium 기능 탐색
     * -> 또한 해당 기능 없을 시 selenium의 action들을 계속 이용해야하는지 체크
     */

    @ApiOperation(value = "목록 조회", response = MonTransaction.class)
    @GetMapping(path = "")
    public CommonResponse getList(@ModelAttribute CommonSearchSpec commonSearchSpec) {
        return new CommonResponse(monTransactionService.getList(commonSearchSpec));
    }

    @ApiOperation(value = "조회", response = Map.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        Optional transaction = monTransactionService.get(seq);
        if (!transaction.isPresent()) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(transaction.get());
    }

    @ApiOperation(value = "생성", response = Map.class)
    @PostMapping(path = "")
    public CommonResponse createTransaction(@RequestParam Map<String, Object> param) {
        return CommonResponse.preparingFunctionResponse();
    }

    @ApiOperation(value = "수정", response = boolean.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonTransaction monTransaction) {
        if (!monTransactionService.edit(monTransaction)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "삭제", response = Map.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        if (!monTransactionService.remove(seq)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "검사 테스트", produces = "multipart/form-data", response = Map.class)
    @PostMapping(value = "/check")
    public CommonResponse transactionCheck(MultipartFile transactionFile) {
        Map<String, Object> resultData = null;
        try {
            List<MonCodeData> codeDataList = new ArrayList<>();
            /*
             * READ CODE FILE
             */
            BufferedReader br;
            String line;
            InputStream is = transactionFile.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            boolean check = false;
            while ((line = br.readLine()) != null) {
                /*
                 * TEST FUNCTION START
                 */
                if (line.indexOf("@Test") > 0) check = true;
                if (check == true) {
                    MonCodeData monCodeData = monTransactionService.getCodeData(line);
                    if (monCodeData != null) {
                        codeDataList.add(monCodeData);
                        log.debug(monCodeData.getAction() + " " + monCodeData.getSelector_type() + "  " + monCodeData.getSelector_value() + "     " + monCodeData.getValue());
                    }
                }
            }
            resultData = monTransactionService.executeTransaction(codeDataList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CommonResponse(resultData);
    }

    @ApiOperation(value = "검사 실행", response = Map.class)
    @GetMapping(path = "/execute/{seq}")
    public CommonResponse transactionExecute(@PathVariable("seq") int seq) {
        return CommonResponse.preparingFunctionResponse();
    }

}