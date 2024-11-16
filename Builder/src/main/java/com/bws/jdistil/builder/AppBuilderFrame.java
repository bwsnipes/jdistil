package com.bws.jdistil.builder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.DataManager;
import com.bws.jdistil.builder.data.Fragment;
import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.generator.AppGenerator;

public class AppBuilderFrame extends JFrame {

	private static final long serialVersionUID = 3454061926559852133L;
	
	private static final Pattern packagePattern = Pattern.compile("^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+[0-9a-z_]$");
	
	private AppGenerator appGenerator = new AppGenerator();
	
	private JMenuItem newMenuItem = null;
	private JMenuItem openMenuItem = null;
	private JMenuItem saveMenuItem = null;
	private JMenuItem generateMenuItem = null;
	private JMenuItem exitMenuItem = null;

	private JTextField projectNameTextField = null;
	private JTextField basePackageTextField = null;
	private JTable fragmentsTable = null;
	
	private JButton addButton = null;
	private JButton updateButton = null;
	private JButton deleteButton = null;
	private JButton relationshipsButton = null;

	private FragmentTableModel fragmentsTableModel = new FragmentTableModel();

	private FragmentDialog fragmentDialog = null;
	private RelationshipsDialog relationshipsDialog = null;
	private ProjectsDialog projectsDialog = null;
	
	private Project workingProject = new Project();
	private String workingProjectName = null;
	
	private List<String> availableProjectNames = new ArrayList<String>();
	private List<String> availableFragmentNames = new ArrayList<String>();
	private Map<String, List<String>> availableAttributeNames = new HashMap<String, List<String>>();
	private List<String> availableCategoryNames = new ArrayList<String>();
	
	public AppBuilderFrame() {
		super("JDistil Application Builder");
		initialize();
	}
	
	private void initialize(){

		// Create dialogs
		fragmentDialog = new FragmentDialog(this);
		relationshipsDialog = new RelationshipsDialog(this);
		projectsDialog = new ProjectsDialog(this);
		
		buildMenuBar(this);
		
		// Set layout for frame
		setLayout(new GridBagLayout());
		  
		// Handle system exit when window is closed
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});
		
		// Create input components
		JLabel projectNameLabel = new JLabel("Project Name");
		projectNameTextField = new JTextField(20);
		  
		JLabel basePackageLabel = new JLabel("Base Package");
		basePackageTextField = new JTextField(30);
		basePackageTextField.setPreferredSize(new Dimension(300, (int)basePackageTextField.getPreferredSize().getHeight()));
		  
		// Create top panel of window layout
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		
		//  Add input components to top panel
		topPanel.add(projectNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(projectNameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		topPanel.add(basePackageLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(basePackageTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		
		// Create fragments table
		fragmentsTable = new JTable(fragmentsTableModel);
		fragmentsTable.setRowSelectionAllowed(true);
		fragmentsTable.setFillsViewportHeight(true);
		fragmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		fragmentsTable.getColumnModel().getColumn(0).setMinWidth(200);
		fragmentsTable.getColumnModel().getColumn(1).setMinWidth(200);
		fragmentsTable.getColumnModel().getColumn(2).setMinWidth(100);
		fragmentsTable.getColumnModel().getColumn(3).setMinWidth(100);
		JScrollPane scrollPane = new JScrollPane(fragmentsTable);

		// Add listener to handle column resizing based on parent size
		fragmentsTable.getParent().addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(final ComponentEvent e) {
		        if (fragmentsTable.getPreferredSize().width < fragmentsTable.getParent().getWidth()) {
		        	fragmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		        } 
		        else {
		        	fragmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		        }
		    }
		});

		// Create button panel of window layout
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		 
		// Create buttons used to edit fragment data
		addButton = new JButton("Add...");
		updateButton = new JButton("Update...");
		deleteButton = new JButton("Delete");
		relationshipsButton = new JButton("Relationships...");
		
		// Create button spacer
		JLabel buttonSpacer = new JLabel();
		buttonSpacer.setPreferredSize(new Dimension(5, 25));
		
		// Add buttons to button panel
		buttonPanel.add(addButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(updateButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(deleteButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(buttonSpacer, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(relationshipsButton, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

		// Add button listeners
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Update reference data
				updateReferenceData(null);
				
				if (fragmentDialog.open(null, availableFragmentNames, availableCategoryNames) == FragmentDialog.OK_BUTTON) {
					
					// Get new fragment
					Fragment fragment = fragmentDialog.getData();
					
					// Add fragment to table data
					fragmentsTableModel.addRowData(fragment);
				}
			}
			
		});
		
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Get selected row
				int row = fragmentsTable.getSelectedRow();
				
				// Get selected fragment
				Fragment fragment = fragmentsTableModel.getRowData(row);

				// Update reference data
				updateReferenceData(fragment.getName());
				
				if (fragmentDialog.open(fragment, availableFragmentNames, availableCategoryNames) == FragmentDialog.OK_BUTTON) {
					
					// Get update fragment
					Fragment updatedFragment = fragmentDialog.getData();
					
					// Update table data with updated fragment
					fragmentsTableModel.setRowData(row, updatedFragment);
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (JOptionPane.showConfirmDialog(deleteButton.getParent(), "Are you sure you want to delete the selected attributes?", 
						"Delete Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					fragmentsTableModel.deleteRowData(fragmentsTable.getSelectedRows());
				}
			}
		});
		
		relationshipsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Update reference data
				updateReferenceData(null);
				
				relationshipsDialog.open(workingProject, availableFragmentNames, availableAttributeNames);
			}
			
		});
		
		// Add listener to handle enabling and disabling buttons based on rows selected
		fragmentsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				updateButtonStates();
			}
			
		});
		
		// Create bottom panel of window layout using a titled border
		JPanel bottomPanel = new JPanel();
		Font font = new JLabel().getFont();
		TitledBorder border = BorderFactory.createTitledBorder("Fragments");
		border.setTitleFont(new Font(font.getFontName(), font.getStyle(), 16));
		bottomPanel.setBorder(border);
		bottomPanel.setLayout(new GridBagLayout());
		
		// Add fragment table and button panel to bottom panel of layout
		bottomPanel.add(scrollPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
		bottomPanel.add(buttonPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		  
		// Add top and bottom panels of layout to app frame
		add(topPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		add(bottomPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
		
		// Set focus to first component
		projectNameTextField.requestFocusInWindow();
		
		updateButtonStates();

		setSize(800, 600);
		setVisible(true);  
	}
	   
	private void buildMenuBar(JFrame frame) {
		
		// Create file menu items
		newMenuItem = new JMenuItem("New Project...");
		openMenuItem = new JMenuItem("Open Project...");
		saveMenuItem = new JMenuItem("Save Project");
		generateMenuItem = new JMenuItem("Generate Project");
		exitMenuItem = new JMenuItem("Exit");
		
		// Create file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(generateMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		
		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (workingProject != null) {
					
					if (JOptionPane.showConfirmDialog(deleteButton.getParent(), "Save changes to existing project before proceeding?", 
							"Save Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						
						saveData();
					}
					
					// Reset project data
					setData(null);
				}
			}
		});
		
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (projectsDialog.open() == ProjectsDialog.OK_BUTTON) {
					
					// Get selected project
					Project project = projectsDialog.getData();
					
					setData(project);
				}
			}
		});
				
		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (workingProject != null) {
					
					saveData();
				}
			}
		});
			
		generateMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (workingProject != null) {
					
					generateArtifacts();
				}
			}
		});
			
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (workingProject != null) {
					
					if (JOptionPane.showConfirmDialog(deleteButton.getParent(), "Save changes to existing project before exiting?", 
							"Save Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						
						// Save project 
						DataManager.getInstance().saveProject(workingProject);
					}
					
					System.exit(0);
				}
			}
		});
		
		// Create menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		
		// Add menu bar to frame
		frame.setJMenuBar(menuBar);
	}
	
	private void updateButtonStates() {
		
		// Get total number of rows selected
		int totalRowsSelected = fragmentsTable.getSelectionModel().getSelectedItemsCount();
		
		// Update button states based on number of rows selected
		updateButton.setEnabled(totalRowsSelected == 1);
		deleteButton.setEnabled(totalRowsSelected >= 1);
		
		boolean isMinimumDataAvailable = fragmentsTableModel.getRowCount() >= 2;
		
		// Update relationships button state based on number of available fragments
		relationshipsButton.setEnabled(isMinimumDataAvailable);
		generateMenuItem.setEnabled(isMinimumDataAvailable);
	}
	
	private void updateReferenceData(String excludedFragmentName) {

		// Clear existing values
		availableFragmentNames.clear();
		availableAttributeNames.clear();
		availableCategoryNames.clear();
		
		for (Fragment fragment : fragmentsTableModel.getRowData()) {
			
			// Get fragment name
			String fragmentName = fragment.getName();
			
			if (excludedFragmentName == null  || !excludedFragmentName.equalsIgnoreCase(fragmentName)) {
				
				// Add fragment name to available list
				availableFragmentNames.add(fragmentName);
			}
			
			for (Attribute attribute : fragment.getAttributes()) {
				
				// Attempt to get attribute names associated with fragment
				List<String> values = availableAttributeNames.get(fragmentName);
				
				if (values == null) {
					
					// Create list of values if they don't already exist
					values = new ArrayList<String>();
					
					// Add values to lookup
					availableAttributeNames.put(fragmentName, values);
				}
				
				// Add attribute name to available attribute names lookup
				values.add(attribute.getName());
				
				// Get category name
				String categoryName = attribute.getLookupCategory();
				
				if (categoryName != null && !availableCategoryNames.contains(categoryName)) {
					
					// Add category name to available list if it doesn't already exist
					availableCategoryNames.add(categoryName);
				}
			}
		}
	}
	
	private void setData(Project data) {

		populateReferenceData();
		
		resetData();

		if (data == null) {
			
			// Set working project to a new instance
			workingProject = new Project();
			
			// Set working project name
			workingProjectName = null;
		}
		else {
			
			// Set working project using provided instance
			workingProject = data;

			// Set working project name
			workingProjectName = data.getName();

			// Populate components using fragment data
			projectNameTextField.setText(workingProject.getName());
			basePackageTextField.setText(workingProject.getBasePackageName());
			
			// Populate fragment table data
			fragmentsTableModel.addRowData(workingProject.getFragments());
		}

		updateButtonStates();
		
		// Set focus to first component
		projectNameTextField.requestFocusInWindow();
	}
	
	private void resetData() {

		availableFragmentNames.clear();
		availableAttributeNames.clear();
		availableCategoryNames.clear();

		// Clear current project
		this.workingProject = null;
		
		// Reset components to initial state
		projectNameTextField.setText(null);
		basePackageTextField.setText(null);
		
		// Clear fragments table data
		fragmentsTableModel.clearData();
	}
	
	private void populateReferenceData() {
		
		// Clear available project names
		availableProjectNames.clear();

		// Get all existing project names
		List<String> projectNames = DataManager.getInstance().findProjectNames();
		
		if (projectNames != null) {
			
			// Set available project names
			availableProjectNames.addAll(projectNames);
		}
	}
	
	private boolean saveData() {
		
		boolean isSaved = false;
		
		if (isValidData()) {
			
			// Populate project with component data
			workingProject.setName(projectNameTextField.getText());
			workingProject.setBasePackageName(basePackageTextField.getText());
			
			// Populate project fragments using fragment table data
			workingProject.setFragments(fragmentsTableModel.getRowData());
			
			// Save project 
			DataManager.getInstance().saveProject(workingProject);

			isSaved = true;
		}
		
		return isSaved;
	}
	
	private void generateArtifacts() {
		
		if (isValidData()) {
			
			try {
				appGenerator.execute(workingProject);
							
			    JOptionPane.showMessageDialog(this, "Artifact generation complete.", "Generate Project", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (Exception exception) {
				
				JOptionPane.showMessageDialog(this, exception.getMessage(), "Errors", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private boolean isValidData() {
		
		List<String> errorMessages = new ArrayList<String>();
		
		if (projectNameTextField.getText() == null || projectNameTextField.getText().isBlank()) {
			errorMessages.add("Project Name is a required field.");
		}
		else if ((workingProjectName == null || !workingProjectName.equalsIgnoreCase(projectNameTextField.getText())) &&
				availableProjectNames.stream().anyMatch(projectNameTextField.getText()::equalsIgnoreCase)) {
			
			errorMessages.add("A project with this name is already defined.");
		}
		
		if (basePackageTextField.getText() == null || basePackageTextField.getText().isBlank()) {
			errorMessages.add("Base Package is a required field.");
		}
		else {
			
			Matcher packageMatcher = packagePattern.matcher(basePackageTextField.getText());
			
			if (!packageMatcher.matches()) {
				errorMessages.add("Invalid base package name.");
			}
		}
		
		
		if (fragmentsTableModel.getRowCount() == 0) {
			errorMessages.add("At least one fragment must be defined.");
		}
		
		// Display errors
		if (!errorMessages.isEmpty()) {
			
			JOptionPane.showMessageDialog(this, errorMessages.toArray(), "Errors", JOptionPane.WARNING_MESSAGE);
		}
		
		return errorMessages.isEmpty();
	}
	
	
private class FragmentTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -4096364364847659463L;

	private String[] columnNames = {"Name", "Parent", "Pagination", "Page Size"};
    private List<Fragment> data = new ArrayList<Fragment>();

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
    	return false;
    }
    
    public void clearData() {
    	
    	data.clear();
    	
    	fireTableDataChanged();
    }
    
    private List<Fragment> getRowData() {
    	
    	return new ArrayList<Fragment>(data);
    }
    
    public Fragment getRowData(int row) {
    
    	Fragment fragment = null;
    	
    	if (row >= 0 && row < data.size()) {
    		
    		fragment = data.get(row);
    	}
    	
    	return fragment;
    }
    
    public void addRowData(Fragment fragment) {
    	
    	if (fragment != null) {
    		
    		data.add(fragment);

        	fireTableDataChanged();
    	}
    }
    
    public void addRowData(List<Fragment> fragments) {
    	
    	if (fragments != null) {
    		
    		data.addAll(fragments);

        	fireTableDataChanged();
    	}
    }
    
    public void setRowData(int row, Fragment fragment) {
    	
    	if (row >= 0 && row < data.size() && fragment != null) {
    		
    		data.set(row, fragment);

        	fireTableDataChanged();
    	}
    }
    
    public void deleteRowData(int[] rows) {
    	
    	if (rows != null) {
    		
    		List<Integer> sortedRows = Arrays.stream(rows).boxed().sorted().toList().reversed();
    		
    		for (Integer row : sortedRows) {
    			
    	    	if (row >= 0 && row < data.size()) {
    	    		
    	    		data.remove(row.intValue());
    	    	}
    		}
    		
        	fireTableDataChanged();
    	}
    }
 
    public Object getValueAt(int row, int col) {
    	
    	String value = null;
    	
    	Fragment fragment = data.get(row);
    	
    	switch (col) {
		case 0: 
    		value = fragment.getName();
    		break;
		case 1: 
    		value = fragment.getParentName();
    		break;
		case 2: 
    		value = fragment.getIsPaginationSupported() ? "Yes" : "No";
    		break;
		case 3: 
    		value = fragment.getPageSize() == null ? "" : fragment.getPageSize().toString();
    		break;
    	}
    	
        return value;
    }

}

	
}
