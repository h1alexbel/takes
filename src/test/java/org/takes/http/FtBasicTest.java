/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.http;

import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.wire.VerboseWire;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.ts.TsFailure;
import org.takes.ts.fork.FkRegex;
import org.takes.ts.fork.TsFork;

/**
 * Test case for {@link FtBasic}.
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
public final class FtBasicTest {

    /**
     * FtBasic can work.
     * @throws Exception If some problem inside
     */
    @Test
    public void justWorks() throws Exception {
        new FtRemote(new TsFork(new FkRegex("/", "hello, world!"))).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    new JdkRequest(home)
                        .through(VerboseWire.class)
                        .fetch()
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK)
                        .assertBody(Matchers.startsWith("hello"));
                }
            }
        );
    }

    /**
     * FtBasic can work with a broken back.
     * @throws Exception If some problem inside
     */
    @Test
    public void gracefullyHandlesBrokenBack() throws Exception {
        new FtRemote(new TsFailure("Jeffrey Lebowski")).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    new JdkRequest(home)
                        .through(VerboseWire.class)
                        .fetch()
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
                        .assertBody(Matchers.containsString("Lebowski"));
                }
            }
        );
    }

}
