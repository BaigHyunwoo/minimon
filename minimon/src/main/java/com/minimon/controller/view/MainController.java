package com.minimon.controller.view;

import com.minimon.common.CommonSearchSpec;
import com.minimon.scheduler.CustomScheduler;
import com.minimon.service.MonActService;
import com.minimon.service.MonApiService;
import com.minimon.service.MonUrlService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/view/main")
@Api(tags = {"View Main Controller"})
public class MainController {

    private final MonUrlService monUrlService;

    private final MonApiService monApiService;

    private final MonActService monActService;

    private final CustomScheduler customScheduler;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public ModelAndView main() {
        CommonSearchSpec commonSearchSpec = new CommonSearchSpec();
        commonSearchSpec.setSortKey("regDate");
        commonSearchSpec.setSortType(Sort.Direction.DESC.name());

        ModelAndView mav = new ModelAndView("view/main/index");
        mav.addObject("urlList", monUrlService.getList(commonSearchSpec));
        mav.addObject("apiList", monApiService.getList(commonSearchSpec));
        mav.addObject("actList", monActService.getList(commonSearchSpec));
        mav.addObject("monList", customScheduler.getRunningScheduler());
        return mav;
    }
}
