package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.common.CommonSearchSpec;
import com.minimon.common.CommonSelenium;
import com.minimon.entity.MonTransaction;
import com.minimon.service.MonTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.*;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/monTransaction")
@Api(tags = {"Monitoring Transaction Controller"})
public class MonTransactionController {

    private final CommonSelenium commonSelenium;
    private final MonTransactionService monTransactionService;


    private static class CompilerClass {
        public CompilerClass() {
        }
    }

    /**
     * TODO
     * 트랜잭션 기능 selenium plugin 이용 -> test.java 파일 업로드 후 compile하여 테스트 실행 및 결과 받아 올 수 있도록 변경
     * 불가능 시 현재 최신 selenium 기능 탐색
     * -> 또한 해당 기능 없을 시 selenium의 action들을 계속 이용해야하는지 체크
     * <p>
     * 파일 저장 -> resource에
     * 파일 경로를 transaction에 저장
     * 경로로 파일을 읽어서 실행
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
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> ds = new DiagnosticCollector<>();
        try (StandardJavaFileManager mgr = compiler.getStandardFileManager(ds, null, null)) {
            Iterable<? extends JavaFileObject> sources = mgr.getJavaFileObjectsFromFiles(Arrays.asList(getTestFile(transactionFile)));
            JavaCompiler.CompilationTask task = compiler.getTask(null, mgr, ds, null, null, sources);
            task.call();

            for (Diagnostic<? extends JavaFileObject> d : ds.getDiagnostics()) {
                System.out.format("Line: %d, %s in %s",
                        d.getLineNumber(), d.getMessage(null),
                        d.getSource().getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return new CommonResponse(resultData);
    }

    public File getTestFile(MultipartFile transactionFile) {
        File sourceFile = null;
        try {

            StringBuffer testSource = new StringBuffer();
            testSource.append("import org.openqa.selenium.support.events.EventFiringWebDriver;\n");
            testSource.append("import com.minimon.common.CommonSelenium;\n");

            BufferedReader br;
            String line;
            InputStream is = transactionFile.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            boolean check = true;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("@Before") > 0) {
                    check = false;
                }
                if (line.indexOf("@Test") > 0) {
                    check = true;
                    continue;
                }
                if (line.indexOf("org.junit") > 0) {
                    continue;
                }
                if (line.indexOf("org.hamcrest") > 0) {
                    continue;
                }
                if (line.indexOf("WebDriver driver") > 0) {
                    String driver = "static WebDriver driver = new CommonSelenium().setUp();\n";
                    testSource.append(driver);
                } else if (check && line.indexOf("public void") > 0) {
                    testSource.append("public static void main(String[] args) {\n");
                } else if (check) {
                    testSource.append(line + "\n");
                }
            }

            System.out.println(testSource);

            String className = "NewsTest";
            // create an empty source file
            sourceFile = File.createTempFile(className,".java");
            sourceFile.deleteOnExit();

            className = sourceFile.getName().split("\\.")[0];
            String sourceCode = testSource.toString();

            FileWriter writer = new FileWriter(sourceFile);
            writer.write(sourceCode);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceFile;
    }

    @ApiOperation(value = "검사 실행", response = Map.class)
    @GetMapping(path = "/execute/{seq}")
    public CommonResponse transactionExecute(@PathVariable("seq") int seq) {
        return CommonResponse.preparingFunctionResponse();
    }
}