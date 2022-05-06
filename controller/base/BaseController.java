package com.wanshun.oaChargingManager.controller.base;

import java.util.ArrayList;
import java.util.List;
import com.wanshun.oaChargingManager.ao.base.BaseFenceAo;
import com.wanshun.oaChargingManager.vo.base.BaseFenceVo;
import com.wanshun.chargingServiceConsoleApi.rpcApi.charteredbusordercharging.RpcCharteredBusOrderChargingConsoleService;
import com.wanshun.chargingServiceConsoleApi.rpcvo.charteredbusordercharging.RpcCharteredConfigConsoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.wanshun.oaChargingManager.ao.base.BaseChannelAo;
import com.wanshun.oaChargingManager.service.account.AccountService;
import com.wanshun.oaChargingManager.vo.base.BaseChannelVo;
import com.wanshun.oaChargingManager.vo.base.BaseCityVo;
import com.wanshun.oaChargingManager.vo.base.RideTypeVo;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.AttributeConst;
import com.wanshun.common.utils.ChargingServiceUtil;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.confservice.rpcVo.RpcCityServiceInfoListConsoleVo;
import com.wanshun.confservice.rpcVo.RpcCityServiceInfoListConsoleVo.RpcCityServiceInfoVo;
import com.wanshun.confservice.rpcVo.RpcLocalAreaConsoleVo;
import com.wanshun.confservice.rpcVo.RpcLocalAreaListConsoleVo;
import com.wanshun.confservice.rpcVo.RpcLocalCityConsoleVo;
import com.wanshun.confservice.rpcVo.RpcLocalCityListConsoleVo;
import com.wanshun.confservice.rpcVo.RpcLocalProvinceConsoleVo;
import com.wanshun.confservice.rpcVo.RpcLocalProvinceListConsoleVo;
import com.wanshun.confservice.rpcapi.RpcCityServiceConsoleService;
import com.wanshun.confservice.rpcapi.RpcLocalAreaConsoleService;
import com.wanshun.confservice.rpcapi.RpcLocalCityConsoleService;
import com.wanshun.confservice.rpcapi.RpcLocalProvinceConsoleService;

@WSRestController(path = "/charging/base", module = "公共查询")
public class BaseController extends AbstractManagerBaseController {

	@Autowired
	private RpcLocalProvinceConsoleService rpcLocalProvinceConsoleService;

	@Autowired
	private RpcLocalCityConsoleService rpcLocalCityConsoleService;

	@Autowired
	private RpcLocalAreaConsoleService rpcLocalAreaConsoleService;

	@Autowired
	private RpcCityServiceConsoleService rpcCityServiceConsoleService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private RpcCharteredBusOrderChargingConsoleService rpcCharteredBusOrderChargingConsoleService;

	@OpRequestSelectMapping(op = "getAllCity", desc = "省市区")
	public BaseCityVo getAllCity() {
		BaseCityVo baseVo = new BaseCityVo();
		// 省
		RpcLocalProvinceListConsoleVo rpcLocalProvinceListConsoleVo = rpcLocalProvinceConsoleService.selectAll();
		List<RpcLocalProvinceConsoleVo> provinceList = rpcLocalProvinceListConsoleVo.getLocalProvinceVos();
		provinceList.removeIf(n -> n.getCode().equals(0));
		baseVo.setProvinceVoList(provinceList);
		// 市
		RpcLocalCityListConsoleVo rpcLocalCityListConsoleVo = rpcLocalCityConsoleService.selectAll();
		List<RpcLocalCityConsoleVo> cityVoList = rpcLocalCityListConsoleVo.getListVo();
		baseVo.setCityVoList(cityVoList);

		RpcCityServiceInfoListConsoleVo rpcListVo = rpcCityServiceConsoleService.selectAll();

		List<Integer> cityCodeList = new ArrayList<>();
		List<RpcCityServiceInfoVo> cityServiceInfoVoList = rpcListVo.getCityServiceInfoVos();
		for (RpcCityServiceInfoVo rpcCityServiceInfoVo : cityServiceInfoVoList) {
			if (rpcCityServiceInfoVo.getAreaSeparateBilling() == 0) {
				cityCodeList.add(rpcCityServiceInfoVo.getCityCode());
			}
		}

		RpcLocalAreaListConsoleVo rpcLocalAreaListConsoleVo = rpcLocalAreaConsoleService.selectAll();
		List<RpcLocalAreaConsoleVo> areaList = rpcLocalAreaListConsoleVo.getList();
		baseVo.setAreaVoList(areaList);
		// 根据城市基础配置区县单独计费是与否来过滤县区数据

		areaList.removeIf(n -> cityCodeList.contains(n.getCityCode()));

		return baseVo;
	}

	@OpRequestSelectMapping(op = "getAllRideType", desc = "服务类型")
	public List<RideTypeVo> getAllRideType() {
		List<RideTypeVo> result = new ArrayList<>();
		AttributeConst.RIDE_TYPE_MAP.forEach((key, value) -> result.add(new RideTypeVo(key, value)));
		return result;
	}

	@OpRequestSelectMapping(op = "openPlatformLike", desc = "开放平台模糊查询")
	public List<BaseChannelVo> getOpenPlatformLike(BaseChannelAo ao) {
		return accountService.getChannelList(ChargingServiceUtil.OPENPLATFORM, ao.getChannelName());
	}

	@OpRequestSelectMapping(op = "enterPriseLike", desc = "企业用车模糊查询")
	public List<BaseChannelVo> getEnterPriseLike(BaseChannelAo ao) {
		return accountService.getChannelList(ChargingServiceUtil.ENTERPRISE, ao.getChannelName());
	}

	@OpRequestSelectMapping(op = "channelLike", desc = "H5渠道商模糊查询")
	public List<BaseChannelVo> getChannelLike(BaseChannelAo ao) {
		return accountService.getChannelList(ChargingServiceUtil.CHANNELPLATFORM, ao.getChannelName());
	}

	@OpRequestSelectMapping(op = "getCharteredConfig", desc = "包车套餐配置")
	public List<RpcCharteredConfigConsoleVo> getCharteredConfig() {
		return rpcCharteredBusOrderChargingConsoleService.getConfigure().getData();
	}

	@OpRequestSelectMapping(op = "fenceLike", desc = "围栏模糊查询")
	public List<BaseFenceVo> getFenceList(BaseFenceAo ao) {
		return accountService.getFenceList(ao.getFenceName());
	}
}
