package simulation;

public class Mc extends OSPABA.IdList {
	//meta! userInfo="Generated code: do not modify", tag="begin"
	public static final int requestResponseMoveWorker = 1021;
	public static final int requestResponseWorkAgentC = 1024;
	public static final int noticeOrderArrival = 1007;
	public static final int requestResponseOrderArrival = 1008;
	public static final int noticeInitAgentOkolie = 1026;
	public static final int requestResponseOrderArrived = 1009;
	public static final int noticeAgentGroupAFreed = 1028;
	public static final int noticeAgentGroupCFreed = 1029;
	public static final int requestResponseFittingAssembly = 1030;
	public static final int noticeWorkstationFreed = 1032;
	public static final int requestResponseFreeWorkstation = 1013;
	public static final int requestResponseWorkAgentA = 1015;
	public static final int requestResponseWorkOnOrderWorkplace = 1016;
	public static final int requestResponseOrderFreeWorkstation = 1017;
	public static final int requestResponseWorkerFreeWorkstation = 1018;
	public static final int requestResponseWorkAgentB = 1020;
	//meta! tag="end"

    // 1..1000 range reserved for user
    public static final int holdOrderArrival = 1;

	// agent A
	public static final int holdPrepareMaterial = 2;
	public static final int holdCutting = 3;
	public static final int holdFitting = 4;

	// agent B
	public static final int holdAssembly = 9;

	// agent C
	public static final int holdMorenie = 7;
	public static final int holdLakovanie = 8;


	public static final int holdMove = 5;
	public static final int holdMoveStorage = 6;
}