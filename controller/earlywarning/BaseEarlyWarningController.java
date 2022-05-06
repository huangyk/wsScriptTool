package com.wanshun.oaChargingManager.controller.earlywarning;

import com.wanshun.authority.control.common.TokenUtils;
import com.wanshun.oaChargingManager.ao.earlywarning.BaseEarlyWarningAo;
import com.wanshun.oaChargingManager.service.account.AccountService;
import com.wanshun.oaChargingManager.vo.earlywarning.BaseEarlyWarningVo;
import com.wanshun.chargingServiceConsoleApi.rpcApi.earlywarning.RpcBaseEarlyWarningConsoleService;
import com.wanshun.chargingServiceConsoleApi.rpcao.earlywarning.RpcBaseEarlyWarningConsoleAo;
import com.wanshun.chargingServiceConsoleApi.rpcvo.earlywarning.RpcBaseEarlyWarningConsoleVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.exception.BusinessException;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.BaseRpcVoUtil;
import com.wanshun.common.utils.StringUtil;
import com.wanshun.common.utils.WsBeanUtil;
import com.wanshun.common.vo.BaseRecordRpcVo;
import com.wanshun.common.vo.BaseRpcVo;
import com.wanshun.common.vo.RpcCountVo;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;
import com.wanshun.manageraccountctrl.common.ManagerTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@WSRestController(path = "/charging/baseEarlyWarning", module = "预警设置")
public class BaseEarlyWarningController extends AbstractManagerBaseController {

    @Autowired
    private RpcBaseEarlyWarningConsoleService rpcBaseEarlyWarningConsoleService;

    @Autowired
    private AccountService accountService;


    @OpRequestSelectMapping(op = "selectPage", desc = "预警设置-分页查询")
    public List<BaseEarlyWarningVo> selectPage(PageModel<BaseEarlyWarningAo> ao) {
        List<BaseEarlyWarningVo> result = new ArrayList<>();

        RpcBaseEarlyWarningConsoleAo rpcAo = new RpcBaseEarlyWarningConsoleAo();
        WsBeanUtil.copyProperties(ao.getBody(), rpcAo);
        rpcAo.setNext(ao.getNext());
        rpcAo.setPageId(StringUtil.isNullOrBlank(ao.getPageId()) ? null : Integer.parseInt(ao.getPageId()));
        rpcAo.setPageSize(ao.getPageSize());

        BaseRecordRpcVo<List<RpcBaseEarlyWarningConsoleVo>> rpcVo = rpcBaseEarlyWarningConsoleService.selectPage(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        rpcVo.getData().forEach(item -> {
            BaseEarlyWarningVo vo = new BaseEarlyWarningVo();
            WsBeanUtil.copyProperties(item, vo);
            vo.setCreateUserName(accountService.getUserNameById(item.getCreateUserId()));
            vo.setUpdateUserName(accountService.getUserNameById(item.getUpdateUserId()));
            result.add(vo);
        });

        return result;
    }


    @OpRequestSelectMapping(op = "selectCount", desc = "预警设置-数量查询")
    public Integer selectCount(PageModel<BaseEarlyWarningAo> ao) {
        RpcBaseEarlyWarningConsoleAo rpcAo = new RpcBaseEarlyWarningConsoleAo();
        WsBeanUtil.copyProperties(ao.getBody(), rpcAo);
        RpcCountVo rpcVo = rpcBaseEarlyWarningConsoleService.selectCount(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);
        return rpcVo.getCount();
    }

    @OpRequestSelectMapping(op = "selectDetils", desc = "预警设置-详情查询")
    public BaseEarlyWarningVo selectDetils(BaseEarlyWarningAo ao) {
        BaseEarlyWarningVo result = new BaseEarlyWarningVo();

        if (null == ao.getId()) {
            throw new BusinessException(AnwserCode.PARAMETER_ERROR);
        }

        RpcBaseEarlyWarningConsoleAo rpcAo = new RpcBaseEarlyWarningConsoleAo();
        rpcAo.setId(ao.getId());
        RpcBaseEarlyWarningConsoleVo rpcVo = rpcBaseEarlyWarningConsoleService.selectDetils(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        WsBeanUtil.copyProperties(rpcVo, result);
        result.setCreateUserName(accountService.getUserNameById(rpcVo.getCreateUserId()));
        result.setUpdateUserName(accountService.getUserNameById(rpcVo.getUpdateUserId()));
        return result;
    }

    @OpRequestUpdateMapping(op = "updateStatus", desc = "预警设置-启用/禁用")
    public AnwserCode updateStatus(BaseEarlyWarningAo ao) {
        if (null == ao.getId() || null == ao.getStatus()) {
            throw new BusinessException(AnwserCode.PARAMETER_ERROR);
        }
        if (ao.getStatus() != ChargingServiceAttributeConst.CHARGING_STATUS_UP && ao.getStatus() != ChargingServiceAttributeConst.CHARGING_STATUS_DOWN) {
            throw new BusinessException(AnwserCode.PARAMETER_ERROR);
        }

        RpcBaseEarlyWarningConsoleAo rpcAo = new RpcBaseEarlyWarningConsoleAo();
        rpcAo.setId(ao.getId());
        rpcAo.setStatus(ao.getStatus());
        rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());

        BaseRpcVo rpcVo = rpcBaseEarlyWarningConsoleService.updateStatus(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);
        return rpcVo.getAnwserCode();
    }

    @OpRequestUpdateMapping(op = "delete", desc = "预警设置-删除")
    public AnwserCode delete(BaseEarlyWarningAo ao) {
        if (null == ao.getId()) {
            throw new BusinessException(AnwserCode.PARAMETER_ERROR);
        }

        RpcBaseEarlyWarningConsoleAo rpcAo = new RpcBaseEarlyWarningConsoleAo();
        rpcAo.setId(ao.getId());
        rpcAo.setStatus(ChargingServiceAttributeConst.CHARGING_STATUS_DEL);
        rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());

        BaseRpcVo rpcVo = rpcBaseEarlyWarningConsoleService.updateStatus(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        return rpcVo.getAnwserCode();
    }

    @OpRequestSaveMapping(op = "save", desc = "预警设置-添加")
    public AnwserCode save(BaseEarlyWarningAo ao) {
        ao.setId(null);
        ao.checkParams();

        RpcBaseEarlyWarningConsoleAo rpcAo = new RpcBaseEarlyWarningConsoleAo();
        WsBeanUtil.copyProperties(ao, rpcAo);
        rpcAo.setCreateUserId(TokenUtils.getCurrentUser().getId());
        rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());
        BaseRpcVo rpcVo = rpcBaseEarlyWarningConsoleService.save(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        return rpcVo.getAnwserCode();
    }

    @OpRequestUpdateMapping(op = "update", desc = "预警设置-修改")
    public AnwserCode update(BaseEarlyWarningAo ao) {
        if (null == ao.getId()) {
            throw new BusinessException(AnwserCode.PARAMETER_ERROR);
        }
        ao.checkParams();

        RpcBaseEarlyWarningConsoleAo rpcAo = new RpcBaseEarlyWarningConsoleAo();
        WsBeanUtil.copyProperties(ao, rpcAo);
        rpcAo.setCreateUserId(TokenUtils.getCurrentUser().getId());
        rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());
        BaseRpcVo rpcVo = rpcBaseEarlyWarningConsoleService.update(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        return rpcVo.getAnwserCode();
    }

}