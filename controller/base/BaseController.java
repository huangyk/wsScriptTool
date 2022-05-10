
@WSRestController(path = "/test", module = "公共查询")
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


}
