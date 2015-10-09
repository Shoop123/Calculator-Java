import javax.swing.JOptionPane;

public class Calculate implements Runnable
{

	public String toParse = "";
	private String answer = "";

	private String[] superScripts = { "⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷",
			"⁸", "⁹" };

	private void parse(String toParse)
	{
		// BEDMAS
		// B
		String brackets = getBrackets(toParse);
		String factorial = getFactorial(brackets);
		String sineAndCosine = getSineAndCosineAndTangent(factorial);
		// E
		String exponents = getExponents(sineAndCosine);
		// DM
		String divisionAndMultiplication = getDivisionAndMultiplication(exponents);
		// AS
		String additionAndSubtraction = getAdditionAndSubtraction(divisionAndMultiplication);

		String finalAnswer = finalize(additionAndSubtraction);

		answer = finalAnswer;
	}

	private String getFactorial(String toParse)
	{
		int index = toParse.indexOf("!");
		String equation = "";
		String original = "";

		while (index > -1)
		{
			equation = "";

			for (int i = index - 1; i >= 0; i--)
			{
				if (toParse.charAt(i) != ' ') equation += toParse.charAt(i);
				else break;
			}

			equation = new StringBuilder(equation).reverse().toString();
			original = equation;

			boolean hasExponent = false;

			for (String s : superScripts)
				if (equation.contains(s))
				{
					hasExponent = true;
					break;
				}

			if (equation.contains("√"))
			{
				hasExponent = true;
				break;
			}

			if (hasExponent) equation = getExponents(equation);

			double factorial = Double.parseDouble(equation);
			double answer = factorial;

			for (double i = factorial - 1; i > 0; i--)
				answer *= i;

			toParse = toParse.replace(original + "!", String.valueOf(answer));

			index = toParse.indexOf("!");
		}

		return toParse;
	}

	private String getBrackets(String toParse)
	{
		int index = toParse.indexOf("(");

		String equation = "";

		boolean restart = false;

		while (index > -1)
		{
			equation = "";
			restart = false;

			for (int i = index + 1; i < toParse.length(); i++)
			{
				if (toParse.charAt(i) == '(')
				{
					restart = true;
					index = i;
					break;
				}
				if (toParse.charAt(i) != ')') equation += toParse.charAt(i);
				else break;
			}

			if (!restart)
			{
				String factorial = getFactorial(equation);

				String sineAndCosine = getSineAndCosineAndTangent(factorial);

				String exponents = getExponents(sineAndCosine);

				String divisionAndMultiplication = getDivisionAndMultiplication(exponents);

				String additionAndSubtraction = getAdditionAndSubtraction(divisionAndMultiplication);

				toParse = toParse.replace("(" + equation + ")",
						additionAndSubtraction);
				index = toParse.indexOf("(");
			}
		}

		return toParse;
	}

	private String getSineAndCosineAndTangent(String toParse)
	{
		boolean sin = false;
		boolean cos = false;
		boolean tan = false;

		int indexS = toParse.indexOf("sin");
		int indexC = toParse.indexOf("cos");
		int indexT = toParse.indexOf("tan");
		int index = -1;

		if (indexS > indexC && indexS > indexT)
		{
			sin = true;
			index = indexS;
		}
		else if (indexC > indexS && indexC > indexT)
		{
			cos = true;
			index = indexC;
		}
		else if (indexT > indexS && indexT > indexC)
		{
			tan = true;
			index = indexT;
		}

		String equation = "";

		while (index > -1)
		{
			equation = "";

			for (int i = index + 3; i < toParse.length(); i++)
			{
				if (toParse.charAt(i) != ' ') equation += toParse.charAt(i);
				else break;
			}

			boolean hasExponents = false;

			for (String s : superScripts)
				if (equation.contains(s))
				{
					hasExponents = true;
					break;
				}

			if (equation.contains("√")) hasExponents = true;

			if (hasExponents) equation = getExponents(equation);

			double answer = 0.0d;

			if (sin)
			{
				answer = Math.sin(Double.parseDouble(equation));
				toParse = toParse.replace("sin" + equation,
						String.valueOf(answer));
			}
			else if (cos)
			{
				answer = Math.cos(Double.parseDouble(equation));
				toParse = toParse.replace("cos" + equation,
						String.valueOf(answer));
			}
			else if (tan)
			{
				answer = Math.tan(Double.parseDouble(equation));
				toParse = toParse.replace("tan" + equation,
						String.valueOf(answer));
			}

			sin = false;
			cos = false;
			tan = false;

			indexS = toParse.indexOf("sin");
			indexC = toParse.indexOf("cos");
			indexT = toParse.indexOf("tan");
			index = -1;

			if (indexS > indexC && indexS > indexT)
			{
				sin = true;
				index = indexS;
			}
			else if (indexC > indexS && indexC > indexT)
			{
				cos = true;
				index = indexC;
			}
			else if (indexT > indexS && indexT > indexC)
			{
				tan = true;
				index = indexT;
			}
		}

		return toParse;
	}

	private String getExponents(String toParse)
	{
		int index = toParse.indexOf("√");
		String equation = "";

		if (index > -1)
		{
			while (index > -1)
			{
				equation = "";

				boolean exponent = false;

				for (int i = index; i < toParse.length(); i++)
				{
					for (String s : superScripts)
						if (String.valueOf(toParse.charAt(i)).equals(s))
						{
							exponent = true;
							break;
						}

					if (!exponent && toParse.charAt(i) != ' ') equation += Character
							.toString(toParse.charAt(i));
					else break;
				}

				double answer = 0.0d;

				String[] split = equation.split("√");
				answer = Math.sqrt(Double.parseDouble(split[1]));

				String answerString = String.valueOf(answer);

				toParse = toParse.replace(equation, answerString);
				index = toParse.indexOf("√");
			}
		}

		String theScript = "";

		index = -1;

		for (int i = 0; i < toParse.length(); i++)
		{
			String letter = String.valueOf(toParse.charAt(i));

			for (int j = 0; j < superScripts.length; j++)
			{
				if (theScript.length() > 0) if (letter.equals(" ")) break;

				if (letter.equals(superScripts[j]))
				{
					theScript += superScripts[j];
					index = i;
				}
			}
		}

		if (index > -1)
		{
			while (index > -1)
			{
				equation = "";

				for (int i = index; i >= 0; i--)
				{
					if (toParse.charAt(i) != ' ') equation += toParse.charAt(i);
					else break;
				}

				equation = new StringBuilder(equation).reverse().toString();

				double answer = 0.0d;

				String thePower = "";

				for (char c : theScript.toCharArray())
					thePower += num(String.valueOf(c));

				if (equation.contains("√"))
				{
					String[] split1 = equation.split("√");
					String[] split2 = split1[1].split(theScript);
					answer = Double.parseDouble(split2[0]);
				}
				else
				{
					String[] split = equation.split(theScript);
					answer = Math.pow(Double.parseDouble(split[0]),
							Double.parseDouble(thePower));
				}

				String answerString = String.valueOf(answer);

				toParse = toParse.replace(equation, answerString);

				index = -1;

				for (int i = 0; i < toParse.length(); i++)
				{
					String letter = String.valueOf(toParse.charAt(i)).trim();

					for (int j = 0; j < superScripts.length; j++)
					{
						if (letter.equals(superScripts[j]))
						{
							theScript = superScripts[j];
							index = i;
							break;
						}
					}
				}
			}
		}

		return toParse;
	}

	private String getDivisionAndMultiplication(String toParse)
	{
		int indexD = toParse.indexOf("÷");
		int indexM = toParse.indexOf("x");
		double answer = 0.0d;
		String equation1 = "";
		String equation2 = "";

		while (indexD > -1 || indexM > -1)
		{
			equation1 = "";
			equation2 = "";

			if ((indexD < indexM && indexD > -1) || indexM == -1)
			{
				for (int i = indexD + 2; i < toParse.length(); i++)
				{
					if (toParse.charAt(i) != ' ') equation2 += toParse
							.charAt(i);
					else break;
				}

				for (int i = indexD - 2; i >= 0; i--)
				{
					if (toParse.charAt(i) != ' ') equation1 += toParse
							.charAt(i);
					else break;
				}

				equation1 = new StringBuilder(equation1).reverse().toString();
				answer = Double.parseDouble(equation1)
						/ Double.parseDouble(equation2);
				toParse = toParse.replaceFirst(equation1 + " ÷ " + equation2,
						String.valueOf(answer));
			}
			else if (indexM > -1)
			{
				for (int i = indexM + 2; i < toParse.length(); i++)
				{
					if (toParse.charAt(i) != ' ') equation2 += toParse
							.charAt(i);
					else break;
				}

				for (int i = indexM - 2; i >= 0; i--)
				{
					if (toParse.charAt(i) != ' ') equation1 += toParse
							.charAt(i);
					else break;
				}

				equation1 = new StringBuilder(equation1).reverse().toString();

				answer = Double.parseDouble(equation1)
						* Double.parseDouble(equation2);

				String answerString = String.valueOf(answer);

				toParse = toParse.replace(equation1 + " x " + equation2,
						answerString);
			}

			indexD = toParse.indexOf("÷");
			indexM = toParse.indexOf("x");
		}

		return toParse;
	}

	private String getAdditionAndSubtraction(String toParse)
	{
		int indexA = toParse.indexOf("+");
		int indexS = toParse.indexOf("- ");
		double answer = 0.0d;
		String equation1 = "";
		String equation2 = "";

		while (indexA > -1 || indexS > -1)
		{
			equation1 = "";
			equation2 = "";

			if ((indexA < indexS && indexA > -1) || indexS == -1)
			{
				for (int i = indexA + 2; i < toParse.length(); i++)
				{
					if (toParse.charAt(i) != ' ') equation2 += toParse
							.charAt(i);
					else break;
				}

				for (int i = indexA - 2; i >= 0; i--)
				{
					if (toParse.charAt(i) != ' ') equation1 += toParse
							.charAt(i);
					else break;
				}

				equation1 = new StringBuilder(equation1).reverse().toString();
				answer = Double.parseDouble(equation1)
						+ Double.parseDouble(equation2);

				String answerString = String.valueOf(answer);
				toParse = toParse.replace(equation1 + " + " + equation2,
						answerString);
			}
			else if (indexS > -1)
			{
				for (int i = indexS + 2; i < toParse.length(); i++)
				{
					if (toParse.charAt(i) != ' ') equation2 += toParse
							.charAt(i);
					else break;
				}

				for (int i = indexS - 2; i >= 0; i--)
				{
					if (toParse.charAt(i) != ' ') equation1 += toParse
							.charAt(i);
					else break;
				}

				equation1 = new StringBuilder(equation1).reverse().toString();
				answer = Double.parseDouble(equation1)
						- Double.parseDouble(equation2);

				String answerString = String.valueOf(answer);
				toParse = toParse.replace(equation1 + " - " + equation2,
						answerString);
			}

			indexA = toParse.indexOf("+");
			indexS = toParse.indexOf("- ");
		}

		return toParse;
	}

	private String finalize(String toParse)
	{
		boolean isInfinity = false;

		if (toParse.contains("Infinity"))
		{
			isInfinity = true;
			toParse = "Infinty";
		}

		if (!isInfinity)
		{
			if (toParse.endsWith(".0")) toParse = toParse.substring(0,
					toParse.length() - 2);

			while (toParse.endsWith("0"))
			{
				if (toParse.contains(".")) toParse = toParse.substring(0,
						toParse.length() - 1);
				else break;
			}
		}

		return toParse;
	}

	private String num(String num)
	{
		switch (num)
		{
		case "⁰":
			return "0";
		case "¹":
			return "1";
		case "²":
			return "2";
		case "³":
			return "3";
		case "⁴":
			return "4";
		case "⁵":
			return "5";
		case "⁶":
			return "6";
		case "⁷":
			return "7";
		case "⁸":
			return "8";
		case "⁹":
			return "9";
		default:
			return "0";
		}
	}

	@Override
	public void run()
	{
		try
		{
			parse(toParse);
			Start.calculator.writeAnswer(answer);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error performing calculation",
					"Incorrect Equation", JOptionPane.ERROR_MESSAGE);
			Start.calculator.writeAnswer(null);
		}
	}
}
