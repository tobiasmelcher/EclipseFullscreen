package melcher;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class ToggleFullscreenAction implements IWorkbenchWindowActionDelegate {
	static boolean enabled=false;
	
	@Override
	public void run(IAction action) {
		enabled = !enabled;
		toggleFullscreen(enabled);
	}
	
	private static void toggleFullscreen(boolean fullScreenEnabled) {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			windows[i].getShell().setFullScreen(fullScreenEnabled);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}
}
