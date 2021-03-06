package com.iflytek.android.framework.volley;

/**
 * Indicates that there was a redirection.
 */
public class RedirectError extends VolleyError {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedirectError() {
    }

    public RedirectError(final Throwable cause) {
        super(cause);
    }

    public RedirectError(final NetworkResponse response) {
        super(response);
    }
}
