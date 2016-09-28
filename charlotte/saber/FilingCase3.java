package charlotte.saber;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.ValueGetter;

public class FilingCase3 implements Closeable {
	private String _domain;
	private int _portno;
	private String _basePath;

	public FilingCase3(String domain, int portno) {
		this(domain, portno, "");
	}

	public FilingCase3(String domain, int portno, String basePath) {
		_domain = domain;
		_portno = portno;
		_basePath = basePath;
	}

	public byte[] get(String path) throws Exception {
		return twice(() -> { return getClient().get(path); });
	}

	public boolean post(String path, byte[] data) throws Exception {
		return twice(() -> { return getClient().post(path, data); });
	}

	public List<String> list(String path) throws Exception {
		return twice(() -> { return getClient().list(path); });
	}

	public boolean delete(String path) throws Exception {
		return twice(() -> { return getClient().delete(path); });
	}

	private <T> T twice(ValueGetter<T> getter) throws Exception {
		try {
			return getter.get();
		}
		catch(Throwable e) {
			closeClient();
			return getter.get();
		}
	}

	private FilingCase3Client _client = null;

	private FilingCase3Client getClient() throws Exception {
		if(_client == null) {
			_client = new FilingCase3Client(_domain, _portno, _basePath);
		}
		return _client;
	}

	private void closeClient() {
		if(_client != null) {
			FileTools.close(_client);
			_client = null;
		}
	}

	@Override
	public void close() throws IOException {
		closeClient();
	}
}
