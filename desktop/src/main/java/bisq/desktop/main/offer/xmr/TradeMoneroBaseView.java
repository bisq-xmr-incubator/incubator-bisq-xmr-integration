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

import bisq.core.locale.Res;
import bisq.core.user.Preferences;
import bisq.desktop.Navigation;
import bisq.desktop.common.model.Activatable;
import bisq.desktop.common.view.ActivatableViewAndModel;
import bisq.desktop.common.view.CachingViewLoader;
import bisq.desktop.common.view.View;
import bisq.desktop.common.view.ViewLoader;
import bisq.desktop.main.MainView;
import bisq.desktop.main.MainView.TradeBaseCurrency;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public abstract class TradeMoneroBaseView extends ActivatableViewAndModel<TabPane, Activatable> {
	
	@FXML
    protected Tab tradeBuyTab, tradeSellTab;

    private Navigation.Listener navigationListener;
    private ChangeListener<Tab> tabChangeListener;

    private final ViewLoader viewLoader;
    private final Navigation navigation;
    private Tab selectedTab;
    private Preferences preferences;
	private TradeBaseCurrency selectedBaseCurrency = TradeBaseCurrency.XMR;
	private Class<? extends OfferXmrView> buyOfferViewClass;
	private Class<? extends OfferXmrView> sellOfferViewClass;

    protected TradeMoneroBaseView(CachingViewLoader viewLoader, Navigation navigation, Preferences preferences, Class<? extends OfferXmrView> buyOfferViewClass, Class<? extends OfferXmrView> sellOfferViewClass) {
        this.viewLoader = viewLoader;
        this.navigation = navigation;
        this.preferences = preferences;
        this.buyOfferViewClass = buyOfferViewClass;
        this.sellOfferViewClass = sellOfferViewClass;
    }

    @Override
    public void initialize() {
    	log.info("initialize({})", selectedTab);
    	root.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        tradeBuyTab.setText(Res.get("mainView.menu.trade.buy", selectedBaseCurrency.toString()).toUpperCase());
        tradeSellTab.setText(Res.get("mainView.menu.trade.sell", selectedBaseCurrency.toString()).toUpperCase());

        if(selectedTab == null) {
        	selectedTab = tradeBuyTab;
        }
        selectView();
        navigationListener = viewPath -> {
        	log.info("viewPath={}, size={}", viewPath, viewPath.size());
            if (viewPath.size() == 2 && navigation.getCurrentPath().get(1) == getClass()) {
            	selectView();
            }
        };

        tabChangeListener = (ov, oldValue, newValue) -> {
        	selectedTab = newValue;
            if (newValue == tradeBuyTab) {
                loadView(buyOfferViewClass);
            } else if (newValue == tradeSellTab) {
                loadView(sellOfferViewClass);
            } else {
                loadView(buyOfferViewClass);
            }
        };       
    }

    private void selectView() {
        if (selectedTab == tradeBuyTab) {
            loadView(buyOfferViewClass);
        } else if (selectedTab == tradeSellTab) {
            loadView(sellOfferViewClass);
        } else {
            loadView(buyOfferViewClass);
        }
	}

	@Override
    protected void activate() {
    	log.info("activate({})", selectedTab);
    	navigation.addListener(navigationListener);
        root.getSelectionModel().selectedItemProperty().addListener(tabChangeListener);

        if (navigation.getCurrentPath().size() == 3 && navigation.getCurrentPath().get(1) == getClass()) {
            Tab selectedItem = root.getSelectionModel().getSelectedItem();
            if (selectedItem == tradeBuyTab) {
            	//TODO(niyid) Replace navigateTo with navigateToWithData passing selectedBaseCurrency and direction (OfferPayload.Direction instance).
             	navigation.navigateTo(MainView.class, getClass(), buyOfferViewClass);
            }
            else if (selectedItem == tradeSellTab) {
            	//TODO(niyid) Replace navigateTo with navigateToWithData passing selectedBaseCurrency and direction (OfferPayload.Direction instance).
               	navigation.navigateTo(MainView.class, getClass(), sellOfferViewClass);
            }
            loadView(navigation.getCurrentPath().get(2));
        }
    }

    @Override
    protected void deactivate() {
    	log.info("deactivate()");
        navigation.removeListener(navigationListener);
        root.getSelectionModel().selectedItemProperty().removeListener(tabChangeListener);
    }

    private void loadView(Class<? extends View> viewClass) {
    	log.info("loadView: " + viewClass);
        if (selectedTab != null && selectedTab.getContent() != null) {
            if (selectedTab.getContent() instanceof ScrollPane) {
                ((ScrollPane) selectedTab.getContent()).setContent(null);
            } else {
                selectedTab.setContent(null);
            }
        }

        View view = viewLoader.load(viewClass);
        if (viewClass == buyOfferViewClass) {
            selectedTab = tradeBuyTab;
        } else if (viewClass == sellOfferViewClass) {
            selectedTab = tradeSellTab;
        }

        selectedTab.setContent(view.getRoot());
        root.getSelectionModel().select(selectedTab);
    }
}

