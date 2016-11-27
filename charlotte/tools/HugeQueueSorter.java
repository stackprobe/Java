package charlotte.tools;

public abstract class HugeQueueSorter extends FileSorter<HugeQueueSorter.Reader, HugeQueueSorter.Writer, byte[]> {
	public void mergeSort(HugeQueue hq) throws Exception {
		String file = FileTools.makeTempPath();
		try {
			Writer w = new Writer(file);
			w.pollAll(hq);
			w.close();

			super.mergeSort(file);

			Reader r = new Reader(file);
			r.addAll(hq);
			r.close();
		}
		finally {
			FileTools.rm(file);
		}
	}

	public static class Reader {
		private HugeQueue.FileReader _reader;

		public Reader(String file) {
			_reader = new HugeQueue.FileReader(file);
		}

		public byte[] read() {
			try {
				return _reader.poll();
			}
			catch(Throwable e) {
				// ignore
			}
			return null;
		}

		public void close() {
			FileTools.close(_reader);
			_reader = null;
		}

		public void addAll(HugeQueue hq) {
			for(; ; ) {
				byte[] block = read();

				if(block == null) {
					break;
				}
				hq.add(block);
			}
		}
	}

	public static class Writer {
		private HugeQueue.FileWriter _writer;

		public Writer(String file) {
			_writer = new HugeQueue.FileWriter(file);
		}

		public void write(byte[] block) {
			_writer.add(block);
		}

		public void close() {
			FileTools.close(_writer);
			_writer = null;
		}

		public void pollAll(HugeQueue hq) {
			for(; ; ) {
				byte[] block = hq.poll();

				if(block == null) {
					break;
				}
				write(block);
			}
		}
	}

	@Override
	protected Reader readOpen(String file) {
		return new Reader(file);
	}

	@Override
	protected byte[] readRecord(Reader reader) {
		return reader.read();
	}

	@Override
	protected void readClose(Reader reader) {
		reader.close();
	}

	@Override
	protected Writer writeOpen(String file) {
		return new Writer(file);
	}

	@Override
	protected void writeRecord(Writer writer, byte[] record) {
		writer.write(record);
	}

	@Override
	protected void writeClose(Writer writer) {
		writer.close();
	}

	@Override
	protected long getWeight(byte[] record) {
		return 100 + record.length;
	}

	@Override
	protected long getWeightMax() {
		return 50000000; // 50 MB
	}
}
