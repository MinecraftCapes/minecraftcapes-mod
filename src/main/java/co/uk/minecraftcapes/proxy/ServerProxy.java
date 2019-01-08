package co.uk.minecraftcapes.proxy;

public class ServerProxy implements CommonProxy {

	@Override
	public void postInit() {
		System.out.println("[MINECRAFTCAPES MOD] THIS MOD IS NOT A SERVER MOD IT IS ONLY CLIENT BASED");
	}

}
