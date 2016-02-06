package melcher;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class TogglePartTitleAction implements IWorkbenchWindowActionDelegate {
	static boolean enabled = false;
	private static ToggleTabHeightRunnable toggleTabHeightRunnable = new ToggleTabHeightRunnable();
	private static int originalTabHeight;
	private static Listener mouseMoveListener;

	@Override
	public void run(IAction action) {
		enabled = !enabled;
		if (enabled) {
			addMouseMoveListener();
		} else {
			removeMouseMoveListener();
			showAllTabHeights();
		}
	}
	
	public static void addMouseMoveListener() {
		mouseMoveListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				Display.getDefault().timerExec(200, toggleTabHeightRunnable);
			}
		};
		Display.getDefault().addFilter(SWT.MouseMove, mouseMoveListener);
	}

	public static void removeMouseMoveListener() {
		if (mouseMoveListener != null) {
			Display.getDefault().removeFilter(SWT.MouseMove, mouseMoveListener);
			mouseMoveListener = null;
		}
	}
	
	private void showAllTabHeights() {
		IWorkbenchWindow aww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		Shell shell = aww.getShell();
		Control[] children = shell.getChildren();
		showAllTabHeights(children);
	}

	private void showAllTabHeights(Control[] children) {
		for (Control child : children) {
			if (child instanceof CTabFolder) {
				CTabFolder t = (CTabFolder) child;
				if (t.getTabHeight() != originalTabHeight) {
					t.setTabHeight(originalTabHeight);
					t.layout();
				}
			} else if (child instanceof Composite) {
				showAllTabHeights(((Composite) child).getChildren());
			}
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
	private static class ToggleTabHeightRunnable implements Runnable {
		@Override
		public void run() {
			IWorkbenchWindow aww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			Point pos = aww.getShell().getDisplay().getCursorLocation();
			Shell shell = aww.getShell();
			Control[] children = shell.getChildren();
			getCTabFolderForPosition(children, pos, shell);
		}

		private void getCTabFolderForPosition(Control[] children, Point pos, Shell shell) {
			for (Control child : children) {
				if (child instanceof CTabFolder) {
					CTabFolder f = (CTabFolder) child;
					int tabHeight = f.getTabHeight();
					Rectangle bounds = f.getBounds();
					Point display = shell.toDisplay(bounds.x, bounds.y);
					Rectangle r = new Rectangle(display.x, display.y, bounds.width, 70);
					if (r.contains(pos)) {
						if (tabHeight != originalTabHeight) {
							f.setTabHeight(originalTabHeight);
							f.layout();
						}
					} else {
						// not contains
						if (tabHeight > 0) {
							originalTabHeight = tabHeight;
							f.setTabHeight(0);
							f.layout();
						}
					}
				} else if (child instanceof Composite) {
					getCTabFolderForPosition(((Composite) child).getChildren(), pos, shell);
				}
			}
		}
	}
}
