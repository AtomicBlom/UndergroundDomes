package net.binaryvibrance.undergrounddomes.generation;

//Converted from c#, because of boxing it was just easier to limit this to floats.

	/// <summary>
	/// Utility methods for arrays.
	/// </summary>
	public final class ArrayUtil
	{
		
		public static String toString(float[] array) {
			return toString(array, "");
		}
		
		/// <summary>
		/// Create a string to display the array values.
		/// </summary>
		/// <param name="array">The array</param>
		/// <param name="format">Optional. A string to use to format each value. Must contain the colon, so something like ':0.000'</param>
		public static String toString(float[] array, String format)
		{
			StringBuilder s = new StringBuilder();
			String formatString = "{0" + format + "}";

			for (int i = 0; i < array.length; i++)
			{
				if (i < array.length - 1)
				{
					s.append(String.format(formatString + ", ", array[i]));
				}
				else
				{
					s.append(String.format(formatString, array[i]));
				}
			}

			return s.toString();
		}
	}
