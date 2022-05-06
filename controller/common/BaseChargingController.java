package com.wanshun.oaChargingManager.controller.common;

import java.util.ArrayList;
import java.util.List;

import com.wanshun.authority.control.common.TokenUtils;
import com.wanshun.oaChargingManager.ao.common.BaseChargingCrowdAo;
import com.wanshun.oaChargingManager.vo.common.BaseChargingCrowdVo;
import com.wanshun.common.utils.*;
import com.wanshun.common.vo.*;
import com.wanshun.constants.platform.ordersystem.carorder.constants.OrderAttributeConst;
import com.wanshun.passengerpersona.ctrlao.crowd.CtrlPCrowdPageListAo;
import com.wanshun.passengerpersona.ctrlapi.crowd.CtrlCrowdService;
import com.wanshun.passengerpersona.ctrlvo.crowd.CtrlPCrowdPageListVo;
import com.wanshun.passengerpersona.enums.PlatformNameEnum;
import com.wanshun.oaChargingManager.ao.common.BaseChargingAo;
import com.wanshun.oaChargingManager.service.account.AccountService;
import com.wanshun.oaChargingManager.vo.common.BaseChargingVo;
import com.wanshun.chargingServiceConsoleApi.rpcApi.common.RpcBaseChargingConsoleService;
import com.wanshun.chargingServiceConsoleApi.rpcao.common.RpcBaseChargingConsoleAo;
import com.wanshun.chargingServiceConsoleApi.rpcvo.common.RpcBaseChargingConsoleVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.exception.BusinessException;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.confservice.rpcapi.RpcLocalAreaConsoleService;
import com.wanshun.confservice.rpcapi.RpcLocalCityConsoleService;
import com.wanshun.confservice.rpcapi.RpcLocalProvinceConsoleService;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;
import com.wanshun.manageraccountctrl.common.ManagerTokenUtil;
import com.wanshun.vpo.chargingservice.common.PrePayRatioPo;
import com.wanshun.vpo.chargingservice.common.ReductionStatusPo;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @Author: zhangruiping
 * @Description: 计价系统基础配置
 * @Date: 2020/9/28 15:13
 * @Version: 1.0
 */
@WSRestController(path = "/charging/baseCharging", module = "基础配置")
public class BaseChargingController extends AbstractManagerBaseController {

	@Resource
	private RpcBaseChargingConsoleService rpcBaseChargingConsoleService;

	@Resource
	private AccountService accountService;

	@Resource
	private RpcLocalProvinceConsoleService rpcLocalProvinceConsoleService;

	@Resource
	private RpcLocalCityConsoleService rpcLocalCityConsoleService;

	@Resource
	private RpcLocalAreaConsoleService rpcLocalAreaConsoleService;

	@Resource
	private CtrlCrowdService ctrlCrowdService;

	@OpRequestSelectMapping(op = "selectPage", desc = "基础配置-分页查询")
	public List<BaseChargingVo> selectPage(PageModel<BaseChargingAo> ao) {
		List<BaseChargingVo> result = new ArrayList<BaseChargingVo>();

		RpcBaseChargingConsoleAo rpcAo = new RpcBaseChargingConsoleAo();
		WsBeanUtil.copyProperties(ao.getBody(), rpcAo);
		rpcAo.setNext(ao.getNext());
		rpcAo.setPageId(StringUtil.isNullOrBlank(ao.getPageId()) ? null : Integer.parseInt(ao.getPageId()));
		rpcAo.setPageSize(ao.getPageSize());
		rpcAo.setCityCodeList(accountService.cityDataPermission());

		BaseRecordRpcVo<List<RpcBaseChargingConsoleVo>> rpcVo = rpcBaseChargingConsoleService.selectPage(rpcAo);
		BaseRpcVoUtil.checkFail(rpcVo);

		rpcVo.getData().forEach(item -> {
			BaseChargingVo vo = new BaseChargingVo();
			WsBeanUtil.copyProperties(item, vo);
			vo.setProvinceName(rpcLocalProvinceConsoleService.findProvinceByCode(item.getProvince()).getName());
			vo.setCityName(rpcLocalCityConsoleService.getNameByCode(item.getCityCode()).getName());
			if (null != item.getAreaCode()) {
				vo.setAreaCodeName(rpcLocalAreaConsoleService.selectAreaCodeByCode(item.getAreaCode()).getName());
			}
			vo.setCreateUserName(accountService.getUserNameById(item.getCreateUserId()));
			vo.setUpdateUserName(accountService.getUserNameById(item.getUpdateUserId()));
			result.add(vo);
		});

		return result;
	}

	@OpRequestSelectMapping(op = "selectCount", desc = "基础配置-数量查询")
	public Integer selectCount(PageModel<BaseChargingAo> ao) {
		RpcBaseChargingConsoleAo rpcAo = new RpcBaseChargingConsoleAo();
		WsBeanUtil.copyProperties(ao.getBody(), rpcAo);
		rpcAo.setCityCodeList(accountService.cityDataPermission());
		RpcCountVo rpcVo = rpcBaseChargingConsoleService.selectCount(rpcAo);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getCount();
	}

	@OpRequestSelectMapping(op = "selectDetils", desc = "基础配置-详情查询")
	public BaseChargingVo selectDetils(BaseChargingAo ao) {
		BaseChargingVo result = new BaseChargingVo();

		if (null == ao.getId()) {
			throw new BusinessException(AnwserCode.PARAMETER_ERROR);
		}

		RpcBaseChargingConsoleAo rpcAo = new RpcBaseChargingConsoleAo();
		rpcAo.setId(ao.getId());
		RpcBaseChargingConsoleVo rpcVo = rpcBaseChargingConsoleService.selectDetils(rpcAo);
		BaseRpcVoUtil.checkFail(rpcVo);

		WsBeanUtil.copyProperties(rpcVo, result);
		result.setPrePayRatioList(JsonUtil.listFromJson(rpcVo.getPrePayRatio(), PrePayRatioPo.class));
		result.setReductionStatusPo(JsonUtil.fromJson(rpcVo.getReductionStatus(), ReductionStatusPo.class));
		result.setProvinceName(rpcLocalProvinceConsoleService.findProvinceByCode(rpcVo.getProvince()).getName());
		result.setCityName(rpcLocalCityConsoleService.getNameByCode(rpcVo.getCityCode()).getName());
		if (null != rpcVo.getAreaCode()) {
			result.setAreaCodeName(rpcLocalAreaConsoleService.selectAreaCodeByCode(rpcVo.getAreaCode()).getName());
		}
		result.setCreateUserName(accountService.getUserNameById(rpcVo.getCreateUserId()));
		result.setUpdateUserName(accountService.getUserNameById(rpcVo.getUpdateUserId()));
		return result;
	}

	@OpRequestUpdateMapping(op = "updateStatus", desc = "基础配置-启用/禁用")
	public AnwserCode updateStatus(BaseChargingAo ao) {
		if (null == ao.getId() || null == ao.getStatus()) {
			throw new BusinessException(AnwserCode.PARAMETER_ERROR);
		}
		if (ao.getStatus() != ChargingServiceAttributeConst.CHARGING_STATUS_UP && ao.getStatus() != ChargingServiceAttributeConst.CHARGING_STATUS_DOWN) {
			throw new BusinessException(AnwserCode.PARAMETER_ERROR);
		}

		RpcBaseChargingConsoleAo rpcAo = new RpcBaseChargingConsoleAo();
		rpcAo.setId(ao.getId());
		rpcAo.setStatus(ao.getStatus());
		rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());

		BaseRpcVo rpcVo = rpcBaseChargingConsoleService.updateStatus(rpcAo);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getAnwserCode();
	}

	@OpRequestUpdateMapping(op = "delete", desc = "基础配置-删除")
	public AnwserCode delete(BaseChargingAo ao) {
		if (null == ao.getId()) {
			throw new BusinessException(AnwserCode.PARAMETER_ERROR);
		}

		RpcBaseChargingConsoleAo rpcAo = new RpcBaseChargingConsoleAo();
		rpcAo.setId(ao.getId());
		rpcAo.setStatus(ChargingServiceAttributeConst.CHARGING_STATUS_DEL);
		rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());

		BaseRpcVo rpcVo = rpcBaseChargingConsoleService.updateStatus(rpcAo);
		BaseRpcVoUtil.checkFail(rpcVo);

		return rpcVo.getAnwserCode();
	}

	@OpRequestSaveMapping(op = "save", desc = "基础配置-添加")
	public AnwserCode save(BaseChargingAo ao) {
		ao.setId(null);
		ao.checkParams(getCrowdVoList(ao), true);
		RpcBaseChargingConsoleAo rpcAo = new RpcBaseChargingConsoleAo();
		WsBeanUtil.copyProperties(ao, rpcAo);
		rpcAo.setReductionStatus(JsonUtil.toJsonExcludeNull(ao.getReductionStatusPo()));
		rpcAo.setPrePayRatio(JsonUtil.toJsonExcludeNull(ao.getPrePayRatioList()));
		rpcAo.setCreateUserId(TokenUtils.getCurrentUser().getId());
		rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());
		BaseRpcVo rpcVo = rpcBaseChargingConsoleService.save(rpcAo);
		BaseRpcVoUtil.checkFail(rpcVo);

		return rpcVo.getAnwserCode();
	}

	@OpRequestUpdateMapping(op = "update", desc = "基础配置-修改")
	public AnwserCode update(BaseChargingAo ao) {
		ao.checkParams(getCrowdVoList(ao), false);
		RpcBaseChargingConsoleAo rpcAo = new RpcBaseChargingConsoleAo();
		WsBeanUtil.copyProperties(ao, rpcAo);
		rpcAo.setReductionStatus(JsonUtil.toJsonExcludeNull(ao.getReductionStatusPo()));
		rpcAo.setPrePayRatio(JsonUtil.toJsonExcludeNull(ao.getPrePayRatioList()));
		rpcAo.setCreateUserId(TokenUtils.getCurrentUser().getId());
		rpcAo.setUpdateUserId(TokenUtils.getCurrentUser().getId());
		BaseRpcVo rpcVo = rpcBaseChargingConsoleService.update(rpcAo);
		BaseRpcVoUtil.checkFail(rpcVo);

		return rpcVo.getAnwserCode();
	}

	private List<BaseChargingCrowdVo> getCrowdVoList(BaseChargingAo ao) {
		List<BaseChargingCrowdVo> result = new ArrayList<>();
		if (!ao.getPrePayRatioSwitch()) {
			return result;
		}

		if (null == ao.getOrderType()) {
			return result;
		}

		if (!ao.getOrderType().equals(OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT) && !ao.getOrderType().equals(OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE)) {
			return result;
		}

		CtrlPCrowdPageListAo ctrlAo = getCtrlPCrowdPageListAo(new BaseChargingCrowdAo());
		ctrlAo.setListPageSize(2000);
		ctrlAo.setListCurrentPage(1);
		BaseListRpcVo<CtrlPCrowdPageListVo> ctrlVo = ctrlCrowdService.queryCrowdListForPage(ctrlAo);
		if (BaseRpcVo.isFail(ctrlVo) || ObjectUtils.isEmpty(ctrlVo.getListVo())) {
			return result;
		}
		result = WsBeanUtil.copyList(ctrlVo.getListVo(), BaseChargingCrowdVo.class);

		return result;
	}

	@OpRequestSelectMapping(op = "getCrowdConfigList", desc = "预付用户群配置")
	public List<BaseChargingCrowdVo> getCrowdConfigList(PageModel<BaseChargingCrowdAo> ao) {
		List<BaseChargingCrowdVo> result = new ArrayList<>();
		CtrlPCrowdPageListAo ctrlAo = getCtrlPCrowdPageListAo(ao.getBody());
		ctrlAo.setListCurrentPage(ao.getCurrentPage());
		ctrlAo.setListPageSize(ao.getPageSize());
		BaseListRpcVo<CtrlPCrowdPageListVo> ctrlVo = ctrlCrowdService.queryCrowdListForPage(ctrlAo);
		if (BaseRpcVo.isFail(ctrlVo) || ObjectUtils.isEmpty(ctrlVo.getListVo())) {
			return result;
		}
		result = WsBeanUtil.copyList(ctrlVo.getListVo(), BaseChargingCrowdVo.class);
		result.forEach(item -> {
			item.setCreateUserName(accountService.getUserNameById(item.getCreateUser()));
		});
		return result;
	}

	@OpRequestSelectMapping(op = "getCrowdConfigCount", desc = "预付用户群配置")
	public Integer getCrowdConfigCount(PageModel<BaseChargingCrowdAo> ao) {
		CtrlPCrowdPageListAo ctrlAo = getCtrlPCrowdPageListAo(ao.getBody());
		BaseDataRpcVo<Integer> ctrlVo = ctrlCrowdService.queryCrowdListForPageCount(ctrlAo);
		return ctrlVo.getData();
	}

	private static CtrlPCrowdPageListAo getCtrlPCrowdPageListAo(BaseChargingCrowdAo ao) {
		CtrlPCrowdPageListAo ctrlAo = new CtrlPCrowdPageListAo();
		ctrlAo.setPlatformCode(PlatformNameEnum.SYS_PRE_PAY.getPlatformCode());
		if (!StringUtil.isNullOrBlank(ao.getCrowdName())) {
			ctrlAo.setCrowdName(ao.getCrowdName());
		}
		if (!StringUtil.isNullOrBlank(ao.getStartCreateTime())) {
			ctrlAo.setYyyyMMddStart(DateUtil.getDateStr_YYYYMMDD_FORMAT(ao.getStartCreateTime()));
		}
		if (!StringUtil.isNullOrBlank(ao.getEndCreateTime())) {
			ctrlAo.setYyyyMMddEnd(DateUtil.getDateStr_YYYYMMDD_FORMAT(ao.getEndCreateTime()));
		}
		return ctrlAo;
	}

}