/**
 * Copyright (c) 2012-2013 William Karnavas 
 * All Rights Reserved
 */

/**
 * 
 * This file is part of MineTunes.
 * 
 * MineTunes is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * MineTunes is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MineTunes. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.minetunes.test;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.minetunes.base64.Base64;

/**
 *
 */
public class Base64Test {

	public static void main(String[] args) {
		try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void test() throws IOException {
		FileDialog d = new FileDialog((Frame) null, "Choose a MIDI:",
				FileDialog.LOAD);
		d.show();
		String fileName = d.getFile();
		if (fileName == null) {
			return;
		}

		File infile = new File(d.getDirectory() + fileName);
		File outfile = new File("C:/users/william/desktop/temp.mid");

		// Copy infile to outfile
		outfile.createNewFile();

		FileInputStream fileIn = new FileInputStream(infile);
		FileOutputStream fileOut = new FileOutputStream(outfile);

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ByteArrayOutputStream inFileBytes = new ByteArrayOutputStream();
		copyStreams(fileIn, inFileBytes);

		String base64InFile = Base64.encodeBytes(inFileBytes.toByteArray(), Base64.GZIP);
		System.out.println (base64InFile);
		byte[] outFileData = Base64.decode(base64InFile);
		
		fileOut.write(outFileData);

		fileOut.flush();
		fileOut.close();
		fileIn.close();
	}

	public static void copyStreams(InputStream in, OutputStream out)
			throws IOException {
		while (true) {
			int byteIn = in.read();
			if (byteIn < 0) {
				break;
			} else {
				//System.out.println(byteIn);
				out.write(byteIn);
			}
		}
	}

}
