/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.plugin.ci.jenkins;

import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;

public class JenkinsServiceFactory extends ServiceFactory<JenkinsClient> {

	public JenkinsServiceFactory() {
		super("jenkins");

	}

	@Override
	protected JenkinsClient doCreateInstance(ServiceDefinition def) {
		JenkinsClientImpl c = new JenkinsClientImpl(def.getProperties()
				.getProperty("url"), def.getProperties()
				.getProperty("username"), def.getProperties().getProperty(
				"password"));

		return c;
	}

}
