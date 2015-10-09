import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Frame extends JFrame
{

	private static final long serialVersionUID = 1L;

	JPanel numberButtonHolder;
	JButton[] numbers = new JButton[10];

	JPanel operationButtonsHolder;
	JButton[] operations = new JButton[10];
	String[] operationSymbols = new String[operations.length];

	JPanel managementButtonsHolder;
	JButton[] managementButtons = new JButton[4];
	String[] managementSymbols = new String[managementButtons.length];

	JPanel extraButtonsHolder;
	JButton[] extraButtons = new JButton[7];
	String[] extraSymbols = new String[extraButtons.length];

	JTextField operation;

	EnterDigit action = new EnterDigit();
	Calculate calc = new Calculate();

	Thread t;

	public Frame(String name)
	{
		super(name);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridLayout(0, 1));
		setLookAndFeel();
		makeInputField();
		makeNumberButtons();
		makeOperationButtons();
		makeManagementButtons();
		makeExtraButtons();
		pack();
		this.setVisible(true);
	}

	private void setLookAndFeel()
	{
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if (info.getName().equals("Nimbus"))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(
							this,
							"Error intializing theme, another one will be used instead",
							"Error", JOptionPane.ERROR_MESSAGE, null);
		}
	}

	private void makeInputField()
	{
		operation = new JTextField();
		operation.setEditable(false);
		Font f = new Font("Arial Rounded MT", Font.PLAIN, 1);
		operation.setFont(f);
		this.add(operation);
	}

	private void assignOperationSymbols()
	{
		operationSymbols[0] = "+";
		operationSymbols[1] = "-";
		operationSymbols[2] = "x";
		operationSymbols[3] = "÷";
		operationSymbols[4] = "√";
		operationSymbols[5] = "x²";
		operationSymbols[6] = "sin";
		operationSymbols[7] = "cos";
		operationSymbols[8] = "tan";
		operationSymbols[9] = ".";
	}

	private void assingManagementSymbols()
	{
		managementSymbols[0] = "=";
		managementSymbols[1] = "D";
		managementSymbols[2] = "C";
		managementSymbols[3] = "Del Selected";
	}

	private void assignExtraSymbols()
	{
		extraSymbols[0] = "(";
		extraSymbols[1] = ")";
		extraSymbols[2] = "xⁿ";
		extraSymbols[3] = "!";
		extraSymbols[4] = "sin";
		extraSymbols[5] = "cos";
		extraSymbols[6] = "tan";
	}

	private void makeExtraButtons()
	{
		assignExtraSymbols();

		extraButtonsHolder = new JPanel();
		extraButtonsHolder.setLayout(new GridLayout());

		for (int i = 0; i < extraButtons.length; i++)
		{
			extraButtons[i] = new JButton(extraSymbols[i]);

			extraButtons[i].addActionListener(action);

			extraButtonsHolder.add(extraButtons[i]);
		}

		this.add(extraButtonsHolder);
	}

	private void makeOperationButtons()
	{
		assignOperationSymbols();

		// create the container and set its layout
		operationButtonsHolder = new JPanel();
		operationButtonsHolder.setLayout(new GridLayout(2, 4));

		// add all of the operations such as multiplication and division
		for (int i = 0; i < operations.length; i++)
		{
			operations[i] = new JButton(" " + operationSymbols[i] + " ");

			if (i == 9) operations[i].setText(operationSymbols[i]);
			else if (i == 4) operations[i].setText(operationSymbols[i]);
			else if (i == 5) operations[i].setText(operationSymbols[i]);

			operations[i].addActionListener(action);

			operationButtonsHolder.add(operations[i]);
		}

		this.add(operationButtonsHolder);
	}

	private void makeNumberButtons()
	{
		int number = 0;

		// create the container and set its layout
		numberButtonHolder = new JPanel();
		numberButtonHolder.setLayout(new GridLayout());

		// add button numbers 1 - 9 and 0
		for (JButton button : numbers)
		{
			number++;

			button = new JButton();

			if (number < 10) button.setText(Integer.toString(number));
			else button.setText("0");

			button.addActionListener(action);

			numberButtonHolder.add(button);
		}

		this.add(numberButtonHolder);
	}

	private void makeManagementButtons()
	{
		assingManagementSymbols();

		managementButtonsHolder = new JPanel();
		managementButtonsHolder.setLayout(new GridLayout());

		for (int i = 0; i < managementButtons.length; i++)
		{
			managementButtons[i] = new JButton(" " + managementSymbols[i] + " ");

			managementButtons[i].addActionListener(action);

			managementButtonsHolder.add(managementButtons[i]);
		}

		this.add(managementButtonsHolder);
	}

	public void writeAnswer(String text)
	{
		if (text != null) operation.setText(text);
		this.setEnabled(true);

		operation.setFont(new Font(operation.getFont().getFontName(), operation
				.getFont().getStyle(), getMaxFontSizeForControl(operation,
				operation.getFontMetrics(operation.getFont())
						.getFontRenderContext()) - 1));
	}

	public int getMaxFontSizeForControl(JTextField field, FontRenderContext frc)
	{
		Rectangle r = field.getBounds();
		Insets m = field.getMargin();
		Insets i = field.getBorder().getBorderInsets(field);
		Rectangle viewableArea = new Rectangle(r.width
				- (m.right + m.left + i.left + i.right), r.height
				- (m.top + m.bottom + i.top + i.bottom));
		Font font = field.getFont();
		int size = 1;
		boolean tooBig = false;
		while (!tooBig)
		{
			Font f = font.deriveFont((float) size);
			GlyphVector gv = f.createGlyphVector(frc, field.getText());
			Rectangle2D box = gv.getVisualBounds();
			if (box.getHeight() > viewableArea.getHeight()
					|| box.getWidth() > viewableArea.getWidth())
			{
				tooBig = true;
				size--;
			}
			size++;
		}
		return size - 1;
	}

	class EnterDigit implements ActionListener
	{
		private boolean plus = false;
		private boolean minus = false;
		private boolean power = false;
		private boolean refactor = false;
		private boolean resize = true;

		@Override
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource() instanceof JButton)
			{
				JButton btn = (JButton) e.getSource();
				String text = btn.getText();
				resize = true;

				if (text.equals("x²")) operation.setText(operation.getText()
						+ "²");
				else if (text.equals("xⁿ")) power = true;
				else if (text.equals(" - "))
				{
					if (operation.getText().length() == 0) operation
							.setText("-");
					else if (plus) operation.setText(operation.getText() + "-");
					else
					{
						minus = true;
						operation.setText(operation.getText() + " - ");
					}
				}
				else if (text.equals(" = "))
				{
					resize = false;
					Start.calculator.setEnabled(false);
					calc.toParse = operation.getText();
					t = new Thread(calc);
					t.start();
				}
				else if (text.equals(" Del Selected "))
				{
					if (operation.getSelectedText() != null) operation
							.setText(operation.getText().replace(
									operation.getSelectedText(), ""));
					resize = false;
				}
				else if (text.equals(" C "))
				{
					resize = false;
					operation.setText("");
				}
				else if (text.equals(" D "))
				{
					resize = false;
					if (operation.getText().length() > 0) operation
							.setText(operation.getText().substring(0,
									operation.getText().length() - 1));
				}
				else if (text.equals(" + "))
				{
					if (minus) operation.setText(operation.getText() + "+");
					else
					{
						plus = true;
						operation.setText(operation.getText() + btn.getText());
					}
				}
				else if (text.equals(" sin ")) operation.setText(operation
						.getText() + "sin");
				else if (text.equals(" cos ")) operation.setText(operation
						.getText() + "cos");
				else if (text.equals(" tan ")) operation.setText(operation
						.getText() + "tan");
				else if (power)
				{
					if (text.equals("1")) operation.setText(operation.getText()
							+ "¹");
					else if (text.equals("2")) operation.setText(operation
							.getText() + "²");
					else if (text.equals("3")) operation.setText(operation
							.getText() + "³");
					else if (text.equals("4")) operation.setText(operation
							.getText() + "⁴");
					else if (text.equals("5")) operation.setText(operation
							.getText() + "⁵");
					else if (text.equals("6")) operation.setText(operation
							.getText() + "⁶");
					else if (text.equals("7")) operation.setText(operation
							.getText() + "⁷");
					else if (text.equals("8")) operation.setText(operation
							.getText() + "⁸");
					else if (text.equals("9")) operation.setText(operation
							.getText() + "⁹");
					else if (text.equals("0")) operation.setText(operation
							.getText() + "⁰");
				}
				else operation.setText(operation.getText() + btn.getText());
			}

			if (refactor)
			{
				plus = false;
				minus = false;
				power = false;
				refactor = false;
			}
			if (plus || minus || power) refactor = true;

			if (resize) operation.setFont(new Font(operation.getFont()
					.getFontName(), operation.getFont().getStyle(),
					getMaxFontSizeForControl(operation, operation
							.getFontMetrics(operation.getFont())
							.getFontRenderContext()) - 1));
		}
	}
}
