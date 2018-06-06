package com.kepler.tcm.server.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class StringList {
	private String lineSeparator = System.getProperty("line.separator");
	private List strings = new ArrayList();
	private List objects = new ArrayList();
	private boolean sorted = false;
	private char nameValueSeparator = '=';

	public void setLineSep(String sep) {
		this.lineSeparator = sep;
	}

	public String[] getStrings() {
		return (String[]) this.strings.toArray(new String[this.strings.size()]);
	}

	public String[] toArray() {
		return getStrings();
	}

	public String[][] toArray2() {
		String[][] result = new String[size()][2];
		for (int i = 0; i < size(); i++) {
			String s = get(i);
			int n = s.indexOf(this.nameValueSeparator);
			if (n == -1) {
				result[i][0] = s;
				result[i][1] = "";
			} else {
				result[i][0] = s.substring(0, n);
				result[i][1] = s.substring(n + 1);
			}
		}

		return result;
	}

	public void setStrings(String[] array) {
		this.strings = null;
		this.objects = null;
		this.strings = new ArrayList(Arrays.asList(array));
		this.objects = new ArrayList(array.length);
	}

	private void rangeCheck(int Index) throws IndexOutOfBoundsException {
		if ((Index < 0) || (Index >= getCount()))
			throw new IndexOutOfBoundsException();
	}

	public int getCount() {
		return this.strings.size();
	}

	public int size() {
		return this.strings.size();
	}

	public String get(int Index) {
		return (String) this.strings.get(Index);
	}

	public int find(String S) {
		int L = 0;
		int H = getCount() - 1;
		while (L <= H) {
			int I = (L + H) / 2;
			int C = get(I).compareTo(S);
			if (C < 0) {
				L = I + 1;
			} else {
				H = I - 1;
				if (C == 0)
					L = I;
			}
		}
		return L;
	}

	public int add(String S, Object AObject) {
		int Result = -1;
		if (!this.sorted)
			Result = getCount();
		else
			Result = find(S);
		insert(Result, S, AObject);
		return Result;
	}

	public int add(String S) {
		return add(S, null);
	}

	public void addStrings(StringList Strings) {
		for (int i = 0; i < Strings.getCount(); i++)
			add(Strings.get(i));
	}

	public void addStrings(String[] Strings) {
		for (int i = 0; i < Strings.length; i++)
			add(Strings[i]);
	}

	public void clear() {
		this.strings.clear();
		this.objects.clear();
	}

	public void delete(int Index) {
		this.strings.remove(Index);
		this.objects.remove(Index);
	}

	public void remove(String s) {
		int n = indexOf(s);
		if (n != -1)
			delete(n);
	}

	public void insert(int Index, String S) {
		this.strings.add(Index, S);
	}

	public void insert(int Index, String S, Object AObject) {
		this.strings.add(Index, S);
		this.objects.add(Index, AObject);
	}

	public void set(int Index, String S) throws IllegalStateException {
		if (this.sorted) {
			throw new IllegalStateException("list sorted!");
		}
		this.strings.set(Index, S);
	}

	public void setObject(int Index, Object AObject) {
		this.objects.set(Index, AObject);
	}

	public void exchange(int Index1, int Index2) {
		Object temp = null;
		temp = this.strings.get(Index1);
		this.strings.set(Index1, this.strings.get(Index2));
		this.strings.set(Index2, temp);
		temp = this.objects.get(Index1);
		this.objects.set(Index1, this.objects.get(Index2));
		this.objects.set(Index2, temp);
	}

	public void quickSort(int L, int R) {
		if (L < R) {
			int i = L;
			int j = R;
			String S = get(L);
			while (i < j) {
				while (get(i).compareTo(S) <= 0)
					i++;
				while (get(j).compareTo(S) > 0)
					j--;
				if (i < j)
					exchange(i, j);
			}
			exchange(i, L);
			if (L < j)
				quickSort(L, j);
			if (i < R)
				quickSort(i, R);
		}
	}

	public void SetSorted(boolean value) {
		if (value != this.sorted) {
			if (value)
				quickSort(0, getCount() - 1);
			this.sorted = value;
		}
	}

	public int indexOf(String S) {
		return this.strings.indexOf(S);
	}

	public String getAllText() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < this.strings.size(); i++) {
			result.append(get(i));
			result.append(this.lineSeparator);
		}
		return result.toString();
	}

	public String getLimitText(int lines) {
		StringBuffer result = new StringBuffer();

		if (lines > 0) {
			if (lines > size())
				return getAllText();
			for (int i = 0; i < lines; i++) {
				result.append(get(i));
				result.append(this.lineSeparator);
			}

		} else {
			lines = -lines;
			if (lines > size())
				return getAllText();
			for (int i = this.strings.size() - lines - 1; i < this.strings.size(); i++) {
				result.append(get(i));
				result.append(this.lineSeparator);
			}

		}

		return result.toString();
	}

	public void addStrings(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, this.lineSeparator);
		while (tokenizer.hasMoreTokens())
			add(tokenizer.nextToken());
	}

	public void setAllText(String value) {
		clear();
		addStrings(value);
	}

	private void saveToFile(FileOutputStream os, String charset) throws IOException {
		if ((charset == null) || (charset.length() == 0)) {
			for (int i = 0; i < this.strings.size(); i++) {
				String s = get(i) + this.lineSeparator;
				os.write(s.getBytes());
			}

		} else {
			for (int i = 0; i < this.strings.size(); i++) {
				String s = get(i) + this.lineSeparator;
				os.write(s.getBytes(charset));
			}
		}

		os.flush();
		os.close();
	}

	public void saveToFile(String filename, String charset) throws IOException {
		saveToFile(new FileOutputStream(filename), charset);
	}

	public void saveToFile(File file, String charset) throws IOException {
		saveToFile(new FileOutputStream(file), charset);
	}

	private void loadFromFile(FileInputStream is, String charset) throws IOException {
		BufferedReader r = null;
		if ((charset == null) || (charset.length() == 0))
			r = new BufferedReader(new InputStreamReader(is));
		else
			r = new BufferedReader(new InputStreamReader(is, charset));
		String line = null;
		clear();
		while ((line = r.readLine()) != null) {
			add(line);
		}
		is.close();
	}

	public void loadFromFile(String filename, String charset) throws IOException {
		loadFromFile(new FileInputStream(filename), charset);
	}

	public void loadFromFile(File file, String charset) throws IOException {
		loadFromFile(new FileInputStream(file), charset);
	}

	public String getName(int i) {
		String s = get(i);
		int n = s.indexOf(this.nameValueSeparator);
		if (n == -1)
			return s;
		return s.substring(0, n);
	}

	public String getValue(int i) {
		String s = get(i);
		int n = s.indexOf(this.nameValueSeparator);
		if (n == -1)
			return null;
		return s.substring(n + 1);
	}

	public String getValue(String name) {
		int n = indexOfName(name);
		if (n != -1)
			return getValue(n);

		return null;
	}

	public void setValue(String name, String value) {
		int n = indexOfName(name);
		String s = name + this.nameValueSeparator + value;
		if (n == -1)
			add(s);
		else
			set(n, s);
	}

	public int indexOfName(String name) {
		for (int i = 0; i < this.strings.size(); i++) {
			String s = get(i);
			int n = s.indexOf(this.nameValueSeparator);

			if ((n == -1) && (s.trim().equals(name)))
				return i;
			if ((n != -1) && (s.substring(0, n).trim().equals(name)))
				return i;

		}

		return -1;
	}

	public void removeByName(String name) {
		int n = indexOfName(name);
		if (n != -1)
			delete(n);
	}

	public void setNameValueSeparator(char separator) {
		this.nameValueSeparator = separator;
	}

	public char getNameValueSeparator() {
		return this.nameValueSeparator;
	}
}