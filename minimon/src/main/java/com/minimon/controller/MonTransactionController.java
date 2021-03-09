package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.entity.MonTransaction;
import com.minimon.repository.MonTransactionRepository;
import com.minimon.service.MonTransactionService;
import com.minimon.service.ResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/monTransaction")
@Api(tags = {"Monitoring Transaction Controller"})
public class MonTransactionController {

    private final MonTransactionRepository monTransactionRepository;

    private final MonTransactionService monTransactionService;

    private final ResultService resultService;

    /**
     * TRANSACTION LIST  호출
     */
    @ApiOperation(value = "목록 조회", response = MonTransaction.class)
    @GetMapping(path = "")
    public CommonResponse getUrls() {
        /**
         HashMap<String, Object> result = new HashMap<String, Object>();

         try {

         List<MonTransaction> transactionList = monTransactionRepository.findAll();
         result.put("transactionList", transactionList);
         result.put("result", "success");


         } catch (Exception e) {

         e.printStackTrace();

         }
         */

        return CommonResponse.preparingFunctionResponse();
    }


    /**
     * transaction 생성
     */
    @ApiOperation(value = "생성", response = Map.class)
    @PostMapping(path = "")
    public CommonResponse createTransaction(@RequestParam Map<String, Object> param) {
        /**
         HashMap<String, Object> result = new HashMap<String, Object>();

         try {

         monTransactionRepository.save(setTblMonTransaction(new MonTransaction(), param));
         result.put("result", "success");

         } catch (Exception e) {

         e.printStackTrace();

         }
         */

        return CommonResponse.preparingFunctionResponse();
    }


    /**
     * transaction INFO  호출
     */
    @ApiOperation(value = "조회", response = Map.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse getTransaction(@PathVariable("seq") int seq) {
        /**
         HashMap<String, Object> result = new HashMap<String, Object>();

         try {


         MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);

         result.put("data", existsTransaction);
         result.put("result", "success");


         } catch (Exception e) {

         e.printStackTrace();

         }
         */

        return CommonResponse.preparingFunctionResponse();
    }


    /**
     * transaction 업데이트
     */
    @ApiOperation(value = "수정", response = Map.class)
    @PutMapping(path = "/{seq}")
    public CommonResponse updateTransaction(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
        /**
         HashMap<String, Object> result = new HashMap<String, Object>();

         try {

         MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);

         if (existsTransaction != null) {

         monTransactionRepository.save(setTblMonTransaction(existsTransaction, param));

         }

         result.put("data", seq);
         result.put("result", "success");


         } catch (Exception e) {

         e.printStackTrace();

         }

         */

        return CommonResponse.preparingFunctionResponse();
    }


    /**
     * transaction 삭제
     */
    @ApiOperation(value = "삭제", response = Map.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        /**
         HashMap<String, Object> result = new HashMap<String, Object>();

         try {


         MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);

         if (existsTransaction != null) {

         monTransactionRepository.delete(existsTransaction);

         }

         result.put("data", seq);
         result.put("result", "success");


         } catch (Exception e) {

         e.printStackTrace();

         }
         */

        return CommonResponse.preparingFunctionResponse();
    }


    /**
     * Upload transaction Code
     */
    @ResponseBody
    @ApiOperation(value = "검사 테스트", produces = "multipart/form-data", response = Map.class)
    @PostMapping(value = "/check")
    public CommonResponse transactionCheck(MultipartFile transactionFile) {

        try {
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
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CommonResponse.preparingFunctionResponse();
    }


    /**
     * transaction  검사 실행
     */
    @ApiOperation(value = "검사 실행", response = Map.class)
    @GetMapping(path = "/execute/{seq}")
    public CommonResponse transactionExecute(@PathVariable("seq") int seq) {
        /**
         HashMap<String, Object> result = new HashMap<String, Object>();

         try {

         MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);

         if (existsTransaction != null) {

         Map<String, Object> logData = transactionService.executeTransaction(existsTransaction.getCodeDataList());
         Map<String, Object> data = transactionService.errorCheckTransaction(existsTransaction, logData);
         result.put("" + existsTransaction.getSeq(), data);

         MonResult monResult = resultService.saveResult(data);
         resultService.sendResultByProperties(monResult);

         }

         result.put("data", seq);
         result.put("result", "success");


         } catch (Exception e) {

         e.printStackTrace();

         }
         */

        return CommonResponse.preparingFunctionResponse();
    }


}