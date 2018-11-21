package de.markusfisch.android.pielauncher.activity;

import de.markusfisch.android.pielauncher.receiver.PackageEventReceiver;
import de.markusfisch.android.pielauncher.widget.AppPieView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.view.Window;

public class HomeActivity extends Activity {
	private static final PackageEventReceiver packageEventReceiver =
			new PackageEventReceiver();

	@Override
	public void onBackPressed() {
		// ignore back on home screen
	}

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(new AppPieView(this));
		setTransparentSystemBars(getWindow());
		registerPackageEventReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterPackageEventReceiver();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static boolean setTransparentSystemBars(Window window) {
		if (window == null ||
				Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			return false;
		}

		window.getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
						View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
						View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		window.setStatusBarColor(0);
		window.setNavigationBarColor(0);

		return true;
	}

	private void registerPackageEventReceiver() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			// broadcasts already declared in manifest
			return;
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_USER_PRESENT);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(packageEventReceiver, filter);
	}

	private void unregisterPackageEventReceiver() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			// broadcasts already declared in manifest
			return;
		}
		unregisterReceiver(packageEventReceiver);
	}
}
