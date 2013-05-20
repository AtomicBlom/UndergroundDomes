//
// Author: Ryan Seghers
//
// Copyright (C) 2013 Ryan Seghers
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the irrevocable, perpetual, worldwide, and royalty-free
// rights to use, copy, modify, merge, publish, distribute, sublicense, 
// display, perform, create derivative works from and/or sell copies of 
// the Software, both in source and object code form, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
// 
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

package net.binaryvibrance.undergrounddomes.generation;

	/// <summary>
	/// A tri-diagonal matrix has non-zero entries only on the main diagonal, the diagonal above the main (super), and the
	/// diagonal below the main (sub).
	/// </summary>
	/// <remarks>
	/// <para>
	/// This is based on the wikipedia article: http://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
	/// </para>
	/// <para>
	/// The entries in the matrix on a particular row are A[i], B[i], and C[i] where i is the row index.
	/// B is the main diagonal, and so for an NxN matrix B is length N and all elements are used.
	/// So for row 0, the first two values are B[0] and C[0].
	/// And for row N-1, the last two values are A[N-1] and B[N-1].
	/// That means that A[0] is not actually on the matrix and is therefore never used, and same with C[N-1].
	/// </para>
	/// </remarks>
	public class TriDiagonalMatrix
	{
		/// <summary>
		/// The values for the sub-diagonal. A[0] is never used.
		/// </summary>
		public float[] a;

		/// <summary>
		/// The values for the main diagonal.
		/// </summary>
		public float[] b;

		/// <summary>
		/// The values for the super-diagonal. C[C.Length-1] is never used.
		/// </summary>
		public float[] c;

		/// <summary>
		/// The width and height of this matrix.
		/// </summary>
		public int getN() {
			return (a != null ? a.length : 0); 
		}

		/// <summary>
		/// Indexer. Setter throws an exception if you try to set any not on the super, main, or sub diagonals.
		/// </summary>
		public float getItem(int row, int col)
			{
				int di = row - col;

				if (di == 0)
				{
					return b[row];
				}
				else if (di == -1)
				{
					assert(row < getN() - 1);
					return c[row];
				}
				else if (di == 1)
				{
					assert(row > 0);
					return a[row];
				}
				else return 0;
			}
		public void setItem(int row, int col, float value) throws Exception {
				int di = row - col;

				if (di == 0)
				{
					b[row] = value;
				}
				else if (di == -1)
				{
					assert row < getN() - 1;
					c[row] = value;
				}
				else if (di == 1)
				{
					assert row > 0;
					a[row] = value;
				}
				else
				{
					throw new Exception("Only the main, super, and sub diagonals can be set.");
				}
			}
		

		/// <summary>
		/// Construct an NxN matrix.
		/// </summary>
		public TriDiagonalMatrix(int n)
		{
			this.a = new float[n];
			this.b = new float[n];
			this.c = new float[n];
		}

		public String toDisplayString() {
			return toDisplayString("", "");			
		}
		
		/// <summary>
		/// Produce a string representation of the contents of this matrix.
		/// </summary>
		/// <param name="fmt">Optional. For String.Format. Must include the colon. Examples are ':0.000' and ',5:0.00' </param>
		/// <param name="prefix">Optional. Per-line indentation prefix.</param>
		public String toDisplayString(String fmt, String prefix)
		{
			if (this.getN() > 0)
			{
				StringBuilder s = new StringBuilder();
				String formatString = "{0" + fmt + "}";

				for (int r = 0; r < getN(); r++)
				{
					s.append(prefix);

					for (int c = 0; c < getN(); c++)
					{
						s.append(String.format(formatString, getItem(r, c)));
						if (c < getN() - 1) s.append(", ");
					}

					s.append("\n");
				}

				return s.toString();
			}
			else
			{
				return prefix + "0x0 Matrix";
			}
		}

		/// <summary>
		/// Solve the system of equations this*x=d given the specified d.
		/// </summary>
		/// <remarks>
		/// Uses the Thomas algorithm described in the wikipedia article: http://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
		/// Not optimized. Not destructive.
		/// </remarks>
		/// <param name="d">Right side of the equation.</param>
		public float[] solve(float[] d) throws Exception
		{
			int n = this.getN();

			if (d.length != n)
			{
				throw new Exception("The input d is not the same size as this matrix.");
			}

			// cPrime
			float[] cPrime = new float[n];
			cPrime[0] = c[0] / b[0];

			for (int i = 1; i < n; i++)
			{
				cPrime[i] = c[i] / (b[i] - cPrime[i-1] * a[i]);
			}

			// dPrime
			float[] dPrime = new float[n];
			dPrime[0] = d[0] / b[0];

			for (int i = 1; i < n; i++)
			{
				dPrime[i] = (d[i] - dPrime[i-1]*a[i]) / (b[i] - cPrime[i - 1] * a[i]);
			}

			// Back substitution
			float[] x = new float[n];
			x[n - 1] = dPrime[n - 1];

			for (int i = n-2; i >= 0; i--)
			{
				x[i] = dPrime[i] - cPrime[i] * x[i + 1];
			}

			return x;
		}
	}

