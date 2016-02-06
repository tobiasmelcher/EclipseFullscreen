package melcher;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class Popup extends Window {

	private Label label;

	public Popup(Shell parentShell) {
		super(parentShell);
		setShellStyle((SWT.NO_TRIM | SWT.ON_TOP | SWT.TOOL) & ~SWT.APPLICATION_MODAL);
	}
	
	@Override
	public int open() {
		Shell shell = getShell();
		if (shell == null || shell.isDisposed()) {
			shell = null;
			// create the window
			create();
			shell = getShell();
		}

		shell.pack(true);

		shell.setVisible(true);

		return OK;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		Color color = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		newShell.setBackground(color);
		newShell.setAlpha(170);
	}
	
	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		
		Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayout(new GridLayout(1, false));
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		contents.setBackground(parent.getBackground());
		
		label = new Label(contents, SWT.NONE);
		label.setBackground(parent.getBackground());
		label.setLayoutData(new GridData(GridData.FILL_BOTH));
		label.setText("Test");
		
		return contents;
	}
	
	public void setText(String text) {
		if (label!=null) {
			label.setText(text);
			getShell().pack(true);
		}
	}
	
	@Override
	protected Point getInitialLocation(Point initialSize) {
		Composite parent = getShell().getParent();

		Monitor monitor = getShell().getDisplay().getPrimaryMonitor();
		if (parent != null) {
			monitor = parent.getMonitor();
		}
		Rectangle monitorBounds = monitor.getClientArea();
		
		return new Point(monitorBounds.x+monitorBounds.width-300, monitorBounds.y+monitorBounds.height-50);
	}
}

