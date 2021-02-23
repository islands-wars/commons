import com.google.gson.annotations.SerializedName;
import java.util.UUID;

/**
 * File <b>Test</b> located on PACKAGE_NAME
 * Test is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2021 Islands Wars.
 * <p>
 * commons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU license</a>.
 * <p>
 *
 * @author Valentin Burgaud (Xharos), {@literal <xharos@islandswars.fr>}
 * Created the 22/02/2021 at 19:22
 * @since TODO edit
 */
public class Test {

	public String name;
	public UUID   id;
	public int    age;
	@SerializedName("prout")
	public long   t;

	public Test() {

	}

	public Test(String name, UUID id, int age, long t) {
		this.name = name;
		this.id = id;
		this.age = age;
		this.t = t;
	}
}
