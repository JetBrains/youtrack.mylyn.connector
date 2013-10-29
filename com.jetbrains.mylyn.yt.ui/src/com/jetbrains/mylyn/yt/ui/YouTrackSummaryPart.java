/**
 * @author: amarch
 */

package com.jetbrains.mylyn.yt.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.mylyn.internal.tasks.ui.editors.EditorUtil;
import org.eclipse.mylyn.internal.tasks.ui.editors.TaskEditorSummaryPart;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.editors.AbstractAttributeEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.jetbrains.mylyn.yt.core.YouTrackTaskDataHandler;



public class YouTrackSummaryPart extends TaskEditorSummaryPart {

  private void addAttribute(Composite composite, FormToolkit toolkit, TaskAttribute attribute,
      boolean shouldInitializeGridData) {
    AbstractAttributeEditor editor = createAttributeEditor(attribute);
    if (editor != null) {
      editor.setReadOnly(true);
      editor.setDecorationEnabled(false);

      editor.createLabelControl(composite, toolkit);
      if (shouldInitializeGridData) {
        GridDataFactory.defaultsFor(editor.getLabelControl())
            .indent(EditorUtil.HEADER_COLUMN_MARGIN, 0).applyTo(editor.getLabelControl());
      }

      editor.createControl(composite, toolkit);
      getTaskEditorPage().getAttributeEditorToolkit().adapt(editor);
    }
  }

  private void addTag(Composite composite, TaskAttribute tag, int horizontalSpan, final int count) {
    if (tag.getId().startsWith(YouTrackTaskDataHandler.TAG_PREFIX) && tag.getValue() != null
        && tag.getValue().length() > 0) {

      CLabel label = new CLabel(composite, SWT.BORDER);
      Image image =
          new Image(label.getDisplay(), getClass().getResourceAsStream("/resource/close_view.gif"));
      label.setImage(image);
      label.setText(tag.getValue());


      label.addMouseListener(new MouseListener() {

        @Override
        public void mouseUp(MouseEvent arg0) {
          CLabel label = (CLabel) arg0.widget;
          Point eventLocation = new Point(label.getBounds().x + arg0.x, arg0.y);

          Image image = label.getImage();

          System.err.println("eventLocation " + eventLocation);
          System.err.println("label.getBounds() " + label.getBounds());
          System.err.println("image.getBounds() " + image.getBounds());

          // check if click is on image
          if (eventLocation.x >= label.getBounds().x + image.getBounds().x
              && eventLocation.x <= label.getBounds().x + image.getBounds().x
                  + image.getBounds().width
              && eventLocation.y >= label.getBounds().y + image.getBounds().y
              && eventLocation.y <= label.getBounds().y + image.getBounds().y
                  + image.getBounds().height) {
            System.err.println("Close tab " + count);
            // label.dispose();
          }
        }

        @Override
        public void mouseDown(MouseEvent arg0) {}

        @Override
        public void mouseDoubleClick(MouseEvent arg0) {}
      });
    }
  }

  @Override
  protected Composite createHeaderLayout(Composite parent, FormToolkit toolkit) {
    Composite composite = toolkit.createComposite(parent);
    GridLayout layout = new GridLayout(1, false);
    layout.verticalSpacing = 1;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    composite.setLayout(layout);
    GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

    // Reporter name
    TaskAttribute attribute =
        getTaskData().getRoot().getMappedAttribute(TaskAttribute.USER_REPORTER);
    if (attribute != null) {
      addAttribute(composite, toolkit, attribute, true);
    }

    // Date of submission
    attribute = getTaskData().getRoot().getMappedAttribute(TaskAttribute.DATE_CREATION);
    if (attribute != null) {
      addAttribute(composite, toolkit, attribute, true);
    }

    // Date of update
    attribute = getTaskData().getRoot().getMappedAttribute(TaskAttribute.DATE_MODIFICATION);
    if (attribute != null) {
      addAttribute(composite, toolkit, attribute, true);
    }

    // Issue URL
    attribute = getTaskData().getRoot().getMappedAttribute("ISSUE_URL");
    if (attribute != null) {
      // TODO: remove hack with attribute text write normal usage
      String url = attribute.getValue();
      attribute.setValue(" ");
      addAttribute(composite, toolkit, attribute, true);
      attribute.setValue(url);
      Link link = new Link(composite, SWT.NONE);
      link.setText(url);
      link.setToolTipText("Open issue in internal Eclipse browser.");
      link.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          try {
            final IWebBrowser browser =
                PlatformUI.getWorkbench().getBrowserSupport()
                    .createBrowser(getTaskData().getRoot().getId());
            browser.openURL(new URL(e.text));
          } catch (PartInitException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
          } catch (MalformedURLException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
          }
        }
      });
    }

    if (layout instanceof GridLayout) {
      GridLayout gl = (GridLayout) layout;
      gl.numColumns = composite.getChildren().length;
      if (gl.numColumns == 0) {
        gl.numColumns = 4;
      }
    }

    if (layout instanceof GridLayout) {
      GridLayout gLayout = (GridLayout) layout;

      Composite secondLineComposite = new Composite(composite, SWT.NONE);
      final RowLayout rowLayout = new RowLayout();
      rowLayout.center = true;
      rowLayout.marginLeft = 0;
      secondLineComposite.setLayout(rowLayout);
      rowLayout.spacing = 8;
      GridDataFactory.fillDefaults().span(gLayout.numColumns, 1).applyTo(secondLineComposite);
      toolkit.adapt(secondLineComposite);

      int count = 0;
      for (String attributeName : getTaskData().getRoot().getAttributes().keySet()) {
        if (attributeName.startsWith(YouTrackTaskDataHandler.TAG_PREFIX)) {
          attribute = getTaskData().getRoot().getMappedAttribute(attributeName);
          if (attribute != null) {
            // addTag(secondLineComposite, attribute, 0, count);
            count++;
          }
        }
      }
    }

    return composite;
  }
}
