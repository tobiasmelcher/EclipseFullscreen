package melcher;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class ToggleTaskbarAction implements IWorkbenchWindowActionDelegate {
	static Control globalTaskBar = null;
	private static Popup popup;
	
	@Override
	public void run(IAction action) {
		toggleTaskBar();
	}
	private static void toggleTaskBar() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (globalTaskBar != null) {
			globalTaskBar.setVisible(true);

			shell.layout();
			globalTaskBar = null;
			popup.close();
			popup = null;
			return;
		}
		Control taskbar = findTaskbar(shell.getChildren());
		taskbar.setVisible(false);
		globalTaskBar = taskbar;
		shell.layout();
		addJobListenerToShowCurrentRunningJob();
	}
	private static Control findTaskbar(Control[] children) {
		Control res = null;
		int max = 0;
		for (Control child : children) {
			int count = getStatusLineNumber((Composite) child);
			if (count > max) {
				max = count;
				res = child;
			}
		}
		return res;
	}

	private static int getStatusLineNumber(Composite control) {
		int count = 0;
		for (Control child : control.getChildren()) {
			String n = child.getClass().getName();
			if (n.contains("StatusLine")) {
				count++;
			}
		}
		return count;
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
	private static void addJobListenerToShowCurrentRunningJob() {
		if (popup == null) {
			popup = new Popup(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		}
		refreshJobStateFrequently();
	}

	private static void refreshJobStateFrequently() {
		Display.getDefault().timerExec(2 * 1000, new Runnable() {

			@Override
			public void run() {
				if (popup == null)
					return;
				String name = "Running ...";
				Job[] jobs = Job.getJobManager().find(null);
				Job job = getFirstRealJob(jobs);
				if (job != null) {
					name = job.getName();
					popup.open();
					popup.setText(name);
				} else {
					popup.close();
				}
				refreshJobStateFrequently();
			}

			private Job getFirstRealJob(Job[] jobs) {
				if (jobs == null)
					return null;
				for (Job j : jobs) {
					if (j.isSystem())
						continue;
					int state = j.getState();
					if (Job.RUNNING == state)
						return j;
				}
				return null;
			}
		});
	}

}
