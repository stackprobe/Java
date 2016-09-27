package charlotte.saber;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import charlotte.tools.FileTools;

public class FilingCase3 implements Closeable {
	private String _domain;
	private int _portno;

	public FilingCase3(String domain, int portno) {
		_domain = domain;
		_portno = portno;
	}

	public byte[] get(final String path) throws Exception {
		try {
			return getClient().get(path);
		}
		catch(Throwable e) {
			fault(e);
		}
		return getClient().get(path);
	}

	public void post(final String path, final byte[] data) throws Exception {
		try {
			getClient().post(path, data);
		}
		catch(Throwable e) {
			fault(e);
		}
		getClient().post(path, data);
	}

	public List<String> list(String path) throws Exception {
		try {
			return getClient().list(path);
		}
		catch(Throwable e) {
			fault(e);
		}
		return getClient().list(path);
	}

	public void delete(String path) throws Exception {
		try {
			getClient().delete(path);
		}
		catch(Throwable e) {
			fault(e);
		}
		getClient().delete(path);
	}

	private void fault(Throwable e) {
		FileTools.close(this);
	}

	private FilingCase3Client _client = null;

	private FilingCase3Client getClient() throws Exception {
		if(_client == null) {
			_client = new FilingCase3Client(_domain, _portno);
		}
		return _client;
	}

	@Override
	public void close() throws IOException {
		if(_client != null) {
			FileTools.close(_client);
			_client = null;
		}
	}
}
