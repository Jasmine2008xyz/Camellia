package com.luoyu.xposed.startup;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam;

public class LSP100Entry extends XposedModule {
  public LSP100Entry(
      XposedInterface xposedInterface, XposedModuleInterface.ModuleLoadedParam moduleLoadedParam) {
        super(xposedInterface,moduleLoadedParam);
      }

  @Override
  public void onPackageLoaded(XposedModuleInterface.PackageLoadedParam param) {
    super.onPackageLoaded(param);
  }
}
