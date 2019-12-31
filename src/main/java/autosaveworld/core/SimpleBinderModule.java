package autosaveworld.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author sarhatabaot
 */
public class SimpleBinderModule extends AbstractModule {
	private final AutoSaveWorld plugin;

	public SimpleBinderModule(final AutoSaveWorld plugin) {
		this.plugin = plugin;
	}


	public Injector createInjector() {
		return Guice.createInjector(this);
	}

	@Override
	protected void configure() {
		// Here we tell Guice to use our plugin instance everytime we need it
		this.bind(AutoSaveWorld.class).toInstance(this.plugin);
	}
}
