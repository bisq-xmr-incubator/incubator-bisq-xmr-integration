/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.desktop.main.offer.xmr;

import javax.inject.Inject;

import bisq.core.user.Preferences;
import bisq.desktop.Navigation;
import bisq.desktop.common.view.CachingViewLoader;
import bisq.desktop.common.view.FxmlView;
import bisq.desktop.main.MainView.TradeBaseCurrency;

@FxmlView
public class TradeXmrBaseView extends TradeMoneroBaseView {

    @Inject
    private TradeXmrBaseView(CachingViewLoader viewLoader, Navigation navigation, Preferences preferences) {
    	super(viewLoader, navigation, preferences, BuyXmrOfferView.class, SellXmrOfferView.class);
    }
}

