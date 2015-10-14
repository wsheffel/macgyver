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
package io.macgyver.plugin.config;

import io.macgyver.plugin.cmdb.AppInstanceController;
import io.macgyver.plugin.cmdb.AppInstanceManager;
import io.macgyver.plugin.cmdb.CmdbApiController;
import io.macgyver.plugin.cmdb.CmdbMenuDecorator;
import io.macgyver.plugin.cmdb.CmdbPlugin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmdbConfig {


	@Bean
	public AppInstanceManager macAppInstanceManager() {
		
		return new AppInstanceManager();
	}
	
	@Bean
	public CmdbApiController macCmdbApiController() {
		return new CmdbApiController();
	}

	
	@Bean
	public CmdbPlugin macCmdbUIManager() {
		return new CmdbPlugin();
	}
	
	@Bean
	public AppInstanceController macAppInstanceController() {
		return new AppInstanceController();
	}
	@Bean
	public CmdbMenuDecorator macCmdbMenuDecorator() {
		return new CmdbMenuDecorator();
	}
}
