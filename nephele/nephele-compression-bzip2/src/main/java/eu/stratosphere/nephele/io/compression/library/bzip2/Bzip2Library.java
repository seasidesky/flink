/***********************************************************************************************************************
 *
 * Copyright (C) 2010 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package eu.stratosphere.nephele.io.compression.library.bzip2;

import eu.stratosphere.nephele.io.compression.CompressionBufferProvider;
import eu.stratosphere.nephele.io.compression.CompressionException;
import eu.stratosphere.nephele.io.compression.CompressionLibrary;
import eu.stratosphere.nephele.io.compression.Compressor;
import eu.stratosphere.nephele.io.compression.Decompressor;
import eu.stratosphere.nephele.util.NativeCodeLoader;
import eu.stratosphere.nephele.util.StringUtils;

public class Bzip2Library implements CompressionLibrary {

	/**
	 * The file name of the native bzip2 library.
	 */
	private static final String NATIVELIBRARYFILENAME = "libbzip2compression.so.1.0";

	public Bzip2Library(final String nativeLibraryDir) throws CompressionException {

		if (!NativeCodeLoader.isLibraryLoaded(NATIVELIBRARYFILENAME)) {
			try {
				NativeCodeLoader.loadLibraryFromFile(nativeLibraryDir, NATIVELIBRARYFILENAME);

				Bzip2Compressor.initIDs();

				Bzip2Decompressor.initIDs();
			} catch (Exception e) {
				throw new CompressionException(StringUtils.stringifyException(e));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUncompressedBufferSize(final int compressedBufferSize) {

		/*
		 * Calculate size of compressed data buffer according to
		 * bzip2 manual
		 */
		return ((compressedBufferSize - 600) / 101) * 100;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLibraryName() {
		return "BZIP2";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Compressor createNewCompressor(final CompressionBufferProvider bufferProvider) throws CompressionException {

		return new Bzip2Compressor(bufferProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Decompressor createNewDecompressor(final CompressionBufferProvider bufferProvider)
			throws CompressionException {

		return new Bzip2Decompressor(bufferProvider);
	}
}
