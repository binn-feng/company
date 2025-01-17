<template>
    <a-modal :open="isSaleQuoteModelVisible" :title="`销售报价 - ${comTitle}`" style="width:70%" @cancel="handleCancel">
        <a-spin v-if="loading" />
        <div v-else>
            <div class="opportunity-introduction">
                <div class="modal-content">
                    <!-- 更新的modal-item样式，使其一行显示三个项目 -->
                    <div class="modal-item" style="width: 30%;">
                        <span>商机名称：</span>
                        <span>{{ opportunity.name }}</span>
                    </div>
                    <div class="modal-item" style="width: 23.3%;">
                        <span>客户名称：</span>
                        <span>{{ opportunity.customersName }}</span>
                    </div>
                    <div class="modal-item" style="width: 23.3%;">
                        <span>产品类别：</span>
                        <span>{{ opportunity.category }}</span>
                    </div>
                    <div class="modal-item" style="width: 23.3%;">
                        <span>所属销售：</span>
                        <span>{{ opportunity.saleName }}</span>
                    </div>

                    <div class="modal-item" style="width: 30%;">
                        <span>所属售前：</span>
                        <span>{{ opportunity.preSaleName }}</span>
                    </div>
                    <div class="modal-item" style="width: 23.3%;">
                        <span>报价版本：</span>
                        <span v-if="isQuoteDetail">{{ currentVersion }}</span>
                        <span v-else>{{ opportunity.currentVersion }}</span>
                    </div>
                    <div class="modal-item" style="width: 23.3%;">
                        <span>报价金额：</span>
                        <span>￥ {{ formatNumber((opportunity.amount || 0).toFixed(2)) }}</span>
                    </div>
                    <div style="width: 23.3% !important;" v-if="opportunity.type === 'ROUGH'">
                        <span>粗略报价说明：</span>
                        <span style="">
                            <!-- <a-textarea style="width: 100%;" :auto-size="{ minRows: 3, maxRows: 5 }" v-model:value="quoteDesc"
                                disabled></a-textarea> -->
                            {{ quoteDesc || '-' }}
                        </span>
                    </div>
                    <div :style="opportunity.type === 'ROUGH' && quoteDesc ? 'width: 90% !important' : 'width: 23.3% !important'"
                        v-if="opportunity.type != 'INCAPABLE' && closeNote">
                        <span>继续跟单理由：</span>
                        <span style="">
                            {{ closeNote || '-' }}
                        </span>
                    </div>
                    <div style="width: 23.3% !important;" v-if="opportunity.type === 'INCAPABLE'">
                        <span>无法报价原因：</span>
                        <!-- <span style="width: 60%;"><a-textarea :auto-size="{ minRows: 3, maxRows: 5 }" v-model:value="quoteDesc"
                                disabled></a-textarea></span> -->
                        <span style="">
                            {{ quoteDesc || '-' }}
                        </span>
                    </div>
                </div>
            </div>
            <!-- <h3>销售报价</h3> -->
            <div style="margin: 10px 20px">
                <!-- <div class="modal-item" style="width: 66.6%"> -->
                <!-- <a-radio-group v-model:value="quoteType" :disabled="isQuoteDisabled || props.isQuoteDetail"> -->
                <a-radio-group v-model:value="quoteType" disabled>
                    <a-radio value="cost">基于成本报价</a-radio>
                    <a-radio value="upload" v-if="isQuoteDisabled">销售试报价</a-radio>
                </a-radio-group>
                <!-- <a-upload v-model:file-list="fileList" name="file" :multiple="true" :headers="headers"
                    @change="handleChange" :show-upload-list="false" :before-upload="(file) => { return false; }">
                    <a>
                        选择文件
                    </a>
                </a-upload>
                <div class="uploaded-files" v-if="fileList.length > 0">
                    <h4>已选择文件:</h4>
                    <ul>
                        <li v-for="file in fileList" :key="file.uid">
                            {{ file.name }}
                            <a-button type="link" @click="removeFile(file.uid)">删除</a-button>
                        </li>
                    </ul>
                </div> -->
            </div>
            <div :class="[{ 'disabled-text': props.isQuoteDetail }]" style="margin: 5px 20px">
                <a-radio-group v-model:value="signType" :disabled="props.isQuoteDetail" style="display: flex;width: 100%;"
                    @change="handleRadioChange">
                    <div style="display: flex; align-items: center; margin-bottom: 10px;">
                        <a-radio :style="radioStyle" :value="'1'" style="flex: 1;">欣象代理</a-radio>
                        <div v-if="signType === '1' && quoteType === 'upload'"
                            style="display: flex; margin-left: 10px; flex: 2;">
                            <a-input-number :step="0.01" :controls="false" style="width: 150px; margin-right: 10px;"
                                placeholder="欣象报价" v-model:value="xxUnableQuoteAmount" @input="updateNorthQuote"
                                :disabled="props.isQuoteDetail"></a-input-number>
                            <!-- <div :class="['nlQuoteStyle', { 'disabled-text': props.isQuoteDetail }]">
                                北光报价：
                            </div> -->
                            <a-input-number :step="0.01" :controls="false" style="width: 150px;margin-right: 20px;"
                                placeholder="北光报价" v-model:value="unableQuoteAmount"
                                :disabled="props.isQuoteDetail"></a-input-number>
                        </div>
                    </div>

                    <div style="display: flex; align-items: center; margin-bottom: 10px;">
                        <a-radio :style="radioStyle" :value="'2'" style="">北光直签</a-radio>
                        <div v-if="signType == '2' && quoteType === 'upload'"
                            style="display: flex; margin-left: 10px; flex: 2;">
                            <a-input-number style="width: 150px" :step="0.01" :controls="false" placeholder="北光报价"
                                v-model:value="unableQuoteAmount" :disabled="props.isQuoteDetail" :precision="2" />
                        </div>
                    </div>
                </a-radio-group>
            </div>
            <div v-if="quoteType === 'cost'">
                <div>
                    <div class="section">
                        <span class="table-title first-title">一、软件费用</span>
                        <table>
                            <thead>
                                <tr>
                                    <th class="profit-quote-column">类目</th>
                                    <th class="profit-quote-column">成本价（北光）</th>
                                    <th class="profit-rate-column">税（%）</th>
                                    <th class="profit-rate-column">利润率 % （北光）</th>
                                    <th class="profit-quote-column">参考报价（北光）</th>
                                    <th class="profit-quote-column" v-if="signType === '1'">销售对外报价(欣象)</th>
                                    <th class="profit-quote-column">销售对外报价(北光)</th>
                                    <th class="profit-rate-column">最终利润率 % （北光）</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>售前支持费用</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.prePrice)) }}</td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.preTaxRate) }}</td>
                                    <td>
                                        <div v-if="isQuoteDetail">
                                            {{ formatPercentage(quotesPresaleInfo.preProfitRate) }}
                                        </div>
                                        <div v-else>
                                            <a-input-number style="width: 100%" :step="0.01" :controls="false"
                                                placeholder="请输入" v-model:value="preProfitRate"
                                                :disabled="props.isQuoteDetail" :precision="2"
                                                @input="handleProfit('pre')" />
                                        </div>
                                    </td>
                                    <td>{{ formatIntNumber(formatNum(quotesPresaleInfo.preSugQuote)) }}</td>
                                    <td v-if="signType === '1'">
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxPreQuote"
                                            @input="updateNorthLightQuote('pre', quotesPresaleInfo.xxPreQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td><a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            v-model:value="quotesPresaleInfo.preExtQuote" :disabled="props.isQuoteDetail"
                                            @input="onUserPerInput(quotesPresaleInfo.preExtQuote)" :precision="0"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.preActualProfitRate) }}</td>
                                </tr>
                                <tr>
                                    <td>定制开发费用</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.devPrice)) }}</td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.devTaxRate) }}</td>
                                    <td>
                                        <div v-if="isQuoteDetail">{{ formatPercentage(quotesPresaleInfo.devProfitRate) }}
                                        </div>
                                        <div v-else>
                                            <a-input-number style="width: 100%" :step="0.01" :controls="false"
                                                placeholder="请输入" v-model:value="devProfitRate"
                                                :disabled="props.isQuoteDetail" :precision="2"
                                                @input="handleProfit('dev')" />
                                        </div>
                                    </td>
                                    <td>{{ formatIntNumber(formatNum(quotesPresaleInfo.devSugQuote)) }}</td>
                                    <td v-if="signType === '1'">
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxDevQuote"
                                            @input="updateNorthLightQuote('dev', quotesPresaleInfo.xxDevQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td><a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            v-model:value="quotesPresaleInfo.devExtQuote" :disabled="props.isQuoteDetail"
                                            @input="onUserDevInput(quotesPresaleInfo.devExtQuote)" :precision="0"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.devActualProfitRate) }}</td>
                                </tr>
                                <tr>
                                    <td>产品平台费用</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.prodPrice)) }}</td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.prodTaxRate) }}</td>
                                    <td>
                                        <div v-if="isQuoteDetail">{{ formatPercentage(quotesPresaleInfo.prodProfitRate) }}
                                        </div>
                                        <div v-else>
                                            <a-input-number style="width: 100%" :step="0.01" :controls="false"
                                                placeholder="请输入" v-model:value="prodProfitRate"
                                                :disabled="props.isQuoteDetail" :precision="2"
                                                @input="handleProfit('prod')" />
                                        </div>
                                    </td>
                                    <td>{{ formatIntNumber(formatNum(quotesPresaleInfo.prodSugQuote)) }}</td>
                                    <td v-if="signType === '1'">
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxProdQuote"
                                            @input="updateNorthLightQuote('prod', quotesPresaleInfo.xxProdQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td>
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            v-model:value="quotesPresaleInfo.prodExtQuote" :disabled="props.isQuoteDetail"
                                            @input="onUserProdInput(quotesPresaleInfo.prodExtQuote)" :precision="0"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.prodActualProfitRate) }}</td>
                                </tr>
                                <tr>
                                    <td>其他费用</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.otherPrice)) }}</td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.otherTaxRate) }}</td>
                                    <td>
                                        <div v-if="isQuoteDetail">{{ formatPercentage(quotesPresaleInfo.otherProfitRate) }}
                                        </div>
                                        <div v-else>
                                            <a-input-number style="width: 100%" :step="0.01" :controls="false"
                                                placeholder="请输入" v-model:value="otherProfitRate"
                                                :disabled="props.isQuoteDetail" :precision="2"
                                                @input="handleProfit('other')" />
                                        </div>
                                    </td>
                                    <td>{{ formatIntNumber(formatNum(quotesPresaleInfo.otherSugQuote)) }}</td>
                                    <td v-if="signType === '1'">
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxOtherQuote"
                                            @input="updateNorthLightQuote('other', quotesPresaleInfo.xxOtherQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td><a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            v-model:value="quotesPresaleInfo.otherExtQuote" :disabled="props.isQuoteDetail"
                                            @input="onUserOtherInput(quotesPresaleInfo.otherExtQuote)" :precision="0"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.otherActualProfitRate) }}</td>
                                </tr>
                                <tr v-if="signType === '1'">
                                    <td>外采硬件代理费补充</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>
                                        <a-input-number style="width: 100%" :step="0.01" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxExtProxyQuote" />
                                    </td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr class="total">
                                    <td>软件费用小计</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.softWareCostTotal)) }}</td>
                                    <td></td>
                                    <td></td>
                                    <td>{{ formatNumber(softWareRefQuoteTotal) }}</td>
                                    <td v-if="signType === '1'">{{
                                        formatNumber(xxSoftWareQuoteTotal) }}</td>
                                    <td>{{ formatNumber(formatNum(softWareExtQuoteTotal)) }}</td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="section">
                        <span class="table-title">二、硬件费用</span>
                        <table>
                            <thead>
                                <tr>
                                    <th class="profit-quote-column">类目</th>
                                    <th class="profit-quote-column">成本价（北光）</th>
                                    <th class="profit-rate-column">税（%）</th>
                                    <th class="profit-rate-column">利润率 % （北光）</th>
                                    <th class="profit-quote-column">参考报价（北光）</th>
                                    <th class="profit-quote-column" v-if="signType === '1'">销售对外报价(欣象)</th>
                                    <th class="profit-quote-column">销售对外报价(北光)</th>
                                    <th class="profit-rate-column">最终利润率 % （北光）</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>硬件费用-自研硬件</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.selfPrice)) }}</td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.selfTaxRate) }}</td>
                                    <td>
                                        <div v-if="isQuoteDetail">{{ formatPercentage(quotesPresaleInfo.selfProfitRate) }}
                                        </div>
                                        <div v-else>
                                            <a-input-number style="width: 100%" :step="0.01" :controls="false"
                                                placeholder="请输入" v-model:value="selfProfitRate"
                                                :disabled="props.isQuoteDetail" :precision="2"
                                                @input="handleProfit('self')" />
                                        </div>
                                    </td>
                                    <td>{{ formatIntNumber(formatNum(quotesPresaleInfo.selfSugQuote)) }}</td>
                                    <td v-if="signType === '1'">
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxSelfQuote"
                                            @input="updateNorthLightQuote('self', quotesPresaleInfo.xxSelfQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td><a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            v-model:value="quotesPresaleInfo.selfExtQuote" :disabled="props.isQuoteDetail"
                                            @input="onUserSelfInput(quotesPresaleInfo.selfExtQuote)" :precision="0"
                                            @keydown="preventDecimal" /></td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.selfActualProfitRate) }}</td>
                                </tr>
                                <tr>
                                    <td>硬件费用-外采硬件</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.extPrice)) }}</td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.extTaxRate) }}</td>
                                    <td>
                                        <div v-if="isQuoteDetail">{{ formatPercentage(quotesPresaleInfo.extProfitRate) }}
                                        </div>
                                        <div v-else>
                                            <a-input-number style="width: 100%" :step="0.01" :controls="false"
                                                placeholder="请输入" v-model:value="extProfitRate" :precision="2"
                                                :disabled="props.isQuoteDetail" @input="handleProfit('ext')" />
                                        </div>
                                    </td>
                                    <td>{{ formatIntNumber(formatNum(quotesPresaleInfo.extSugQuote)) }}</td>
                                    <td v-if="signType === '1'">
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxExtQuote"
                                            @input="updateNorthLightQuote('ext', quotesPresaleInfo.xxExtQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td><a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            v-model:value="quotesPresaleInfo.extExtQuote" :disabled="props.isQuoteDetail"
                                            @input="onUserExtInput(quotesPresaleInfo.extExtQuote)" :precision="0"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.extActualProfitRate) }}</td>
                                </tr>
                                <tr class="total">
                                    <td>硬件费用小计</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.hardWareCostTotal)) }}</td>
                                    <td></td>
                                    <td class="profit-rate-column"></td>
                                    <td>{{ formatNumber(hardWareRefQuoteTotal) }}</td>
                                    <td v-if="signType === '1'">{{
                                        formatNumber(xxHardWareQuoteTotal) }}</td>
                                    <td>{{ formatNumber(formatNum(hardWareExtQuoteTotal)) }}</td>
                                    <td> </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section">
                        <span class="table-title">三、实施费用</span>
                        <table>
                            <thead>
                                <tr>
                                    <th class="profit-quote-column">类目</th>
                                    <th class="profit-quote-column">成本价（北光）</th>
                                    <th class="profit-rate-column">税（%）</th>
                                    <th class="profit-rate-column">利润率 % （北光）</th>
                                    <th class="profit-quote-column">参考报价（北光）</th>
                                    <th class="profit-quote-column" v-if="signType === '1'">销售对外报价(欣象)</th>
                                    <th class="profit-quote-column">销售对外报价(北光)</th>
                                    <th class="profit-rate-column">最终利润率 % (北光)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>实施费用</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.impPrice)) }}</td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.impTaxRate) }}</td>
                                    <td>
                                        <div v-if="isQuoteDetail">{{ formatPercentage(quotesPresaleInfo.impProfitRate) }}
                                        </div>
                                        <div v-else>
                                            <a-input-number style="width: 100%" :step="0.01" :controls="false"
                                                placeholder="请输入" v-model:value="impProfitRate"
                                                :disabled="props.isQuoteDetail" :precision="2"
                                                @input="handleProfit('imp')" />
                                        </div>
                                    </td>
                                    <td>{{ formatIntNumber(formatNum(quotesPresaleInfo.impSugQuote)) }}</td>
                                    <td v-if="signType === '1'">
                                        <a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxImpQuote"
                                            @input="updateNorthLightQuote('imp', quotesPresaleInfo.xxImpQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td><a-input-number style="width: 100%" :step="1" :controls="false" placeholder="请输入"
                                            v-model:value="quotesPresaleInfo.impExtQuote" :disabled="props.isQuoteDetail"
                                            :precision="0" @input="onUserImpInput(quotesPresaleInfo.impExtQuote)"
                                            @keydown="preventDecimal" />
                                    </td>
                                    <td>{{ formatPercentage(quotesPresaleInfo.impActualProfitRate) }}</td>
                                </tr>
                                <tr class="total">
                                    <td>实施费用小计</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.impPrice)) }}</td>
                                    <td></td>
                                    <td></td>
                                    <td>{{ formatNumber(implementationRefQuoteTotal) }}</td>
                                    <td v-if="signType === '1'">
                                        {{ formatNumber(formatNum(quotesPresaleInfo.xxImpQuote)) }}
                                    </td>
                                    <td>{{ formatNumber(formatNum(implementationExtQuoteTotal)) }}</td>
                                    <td> </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section" v-if="signType === '1'">
                        <span class="table-title">四、其他费用</span>
                        <table>
                            <thead>
                                <tr>
                                    <th class="profit-quote-column">类目</th>
                                    <th class="profit-quote-column">成本价（北光）</th>
                                    <th class="profit-rate-column">税（%）</th>
                                    <th class="profit-rate-column">利润率 % （北光）</th>
                                    <th class="profit-quote-column">参考报价（北光）</th>
                                    <th class="profit-quote-column">销售对外报价(欣象)</th>
                                    <th class="profit-quote-column">销售对外报价(北光)</th>
                                    <th class="profit-rate-column">最终利润率 % (北光)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><span style="color:red">* </span>其他费用</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>
                                        <a-input-number style="width: 100%; border: 2px solid rgb(235, 158, 158)" :step="1" :controls="false" placeholder="请输入"
                                            :disabled="props.isQuoteDetail" :precision="0"
                                            v-model:value="quotesPresaleInfo.xxAdditionalQuote" @keydown="preventDecimal" />
                                    </td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr class="total">
                                    <td>其他费用小计</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td v-if="signType === '1'">
                                        {{ formatNumber(formatNum(quotesPresaleInfo.xxAdditionalQuote)) }}
                                    </td>
                                    <td>-</td>
                                    <td> </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section">
                        <span class="table-title">对外报价合计</span>
                        <table style="background-color:#f1f1f1;" class="total">
                            <tbody>
                                <tr>
                                    <td class="total" style="width: 300px">欣象销售对外报价合计</td>
                                    <td colspan="6">
                                        <span>
                                            {{ signType === '1' ? formatNumber(formatNum(proXxAllExtQuoteTotal)) : '-' }}
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="total" style="width: 300px">北光销售对外报价合计</td>
                                    <td colspan="6">
                                        <span>
                                            {{ formatNumber(formatNum(proBgAllExtQuoteTotal)) }}
                                        </span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section">
                        <span class="table-title">北光对外报价合计</span>
                        <table style="background-color:#f1f1f1;" class="total">
                            <tbody>
                                <tr>
                                    <td>北光项目成本合计</td>
                                    <td>{{ formatNumber(formatNum(quotesPresaleInfo.projCostTotal)) }}</td>
                                    <td>北光项目建议报价</td>
                                    <td colspan="2">{{ formatNumber(formatNum(quotesPresaleInfo.projSugQuoteTotal)) }}</td>
                                    <td>北光销售对外报价合计</td>
                                    <td colspan="2">{{ formatNumber(formatNum(projExtQuoteTotal)) }}</td>
                                </tr>
                                <tr>
                                    <td>北光项目利润率（不含外采硬件）</td>
                                    <td colspan="6" :class="{
                                        'highlight': isProfitRateLow
                                    }">
                                        <span v-if="props.isQuoteDetail">
                                            {{ formatPercentage(quotesPresaleInfo.projProfitRateExcl) }}
                                        </span>
                                        <span v-else>
                                            {{ formatPercentage(projProfitRateExcl) }}
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>北光项目利润率（含外采硬件）</td>
                                    <td colspan="6">
                                        <span v-if="props.isQuoteDetail">
                                            {{ formatPercentage(quotesPresaleInfo.projProfitRateIncl) }}
                                        </span>
                                        <span v-else>
                                            {{ formatPercentage(projProfitRateIncl) }}
                                        </span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div v-if="quoteType === 'upload'" class="upload-btn">
                <div style="padding-bottom:10px; display: flex; align-items: flex-start;">
                    <span style="width: 15%;">销售试报价说明：</span>
                    <span style="width: 60%;">
                        <a-textarea :auto-size="{ minRows: 3, maxRows: 5 }" v-model:value="description"
                            :disabled="props.isQuoteDetail"></a-textarea>
                    </span>
                </div>
            </div>
        </div>
        <template #footer>
            <div style="margin: 40px 0 20px">
                <a-button type="default" @click="handleCancel">取消</a-button>
                <a-button type="primary" @click="handleQuoteOk" v-if="props.isSales">确认销售报价</a-button>
                <a-button type="primary" @click="handleReapply" v-if="props.isSales">申请重新报价</a-button>
                <a-button type="primary" @click="handleLose" v-if="props.isSales">丢单处理</a-button>
            </div>
        </template>
        <DocumentaryModel :open="isDocumentaryModellVisible" @close="handleDocumentClose"
            :projProfitRateExcl="projProfitRateExcl" :opportunity="props.opportunity" @confirmQuote="confirmQuote"
            :externalProfit="externalProfit" />
    </a-modal>
</template>

<script setup>
import { defineProps, ref, defineEmits, watch, computed, reactive } from 'vue';
import DocumentaryModel from './DocumentaryModel.vue'
import { fetchSalesDetailInfo, fetchSalesRoughInfo, confirmSalesQuote, getSalesDetailInfoById, fetchReSalesInfo } from '@/api/saleQoute'
import { message } from 'ant-design-vue';
// import { fetchUploads } from '@/api/common';
import { getProfit } from '@/api/mannageDebug';
import { formatNumber, formatIntNumber } from '@/utils/format'
// import {getMap} from '@/api/mannageDebug' //报价设置（1.06 1.13）

const radioStyle = reactive({
    display: 'flex',
    height: '30px',
    lineHeight: '30px',
});
const props = defineProps({
    opportunity: {
        type: Object,
        required: true
    },
    // 进行报价或者重新报价
    isSales: {
        type: Boolean,
        required: false
    },
    // 查看详情
    isQuoteDetail: {
        type: Boolean,
        required: false
    },
    selectedSalesId: {
        type: String,
        required: false
    },
    // 粗略销售报价
    isRoughSalesQuote: {
        type: Boolean,
        required: false
    },
    // 更新销售报价
    isUpdateSalesOffer: {
        type: Boolean,
        required: false
    },
});
// 页面参数
const quotesPresaleInfo = ref({
    closeNote: '',
    opportunitiesId: null,
    devExtQuote: null, // 定制开发对外报价 
    devPrice: null,  // 定制开发成本价
    devTaxRate: null,  //定制开发税率
    devProfitRate: null,  // 定制开发利润率
    xxDevQuote: null,//欣象定制开发报价
    devSugQuote: null,  // 定制开发建议报价 
    devActualProfitRate: null,//定制开发最终利润率
    extExtQuote: null, // 外采对外报价 
    extPrice: null,
    extTaxRate: null,
    extProfitRate: null,
    xxExtQuote: null,//欣象外采报价
    extSugQuote: null,
    extActualProfitRate: null,//外采最终利润率
    impExtQuote: null, // 实施对外报价 
    impPrice: null,
    impTaxRate: null,
    impProfitRate: null,
    xxImpQuote: null,//欣象实施报价
    impSugQuote: null,
    impActualProfitRate: null,//实施最终利润率
    otherExtQuote: null,//其他对外报价
    otherPrice: null,
    otherTaxRate: null,
    otherProfitRate: null,
    xxOtherQuote: null,//欣象其他报价
    otherSugQuote: null,
    otherActualProfitRate: null,//其他最终利润率
    xxExtProxyQuote: null, // 外采硬件代理费补充-销售对外报价(欣象)
    preExtQuote: null,//售前支持
    prePrice: null,
    preTaxRate: null,
    preProfitRate: null,
    xxPreQuote: null,//欣象售前支持报价
    preSugQuote: null,
    preActualProfitRate: null,//售前支持最终利润率
    prodExtQuote: null,//产品
    prodPrice: null,
    prodTaxRate: null,
    prodProfitRate: null,
    xxProdQuote: null,//欣象产品平台报价
    prodSugQuote: null,
    prodActualProfitRate: null,//产品最终利润率
    selfExtQuote: null,//自研
    selfPrice: null,
    selfTaxRate: null,
    selfProfitRate: null,
    xxSelfQuote: null,//欣象自研报价
    selfSugQuote: null,
    selfActualProfitRate: null,//自研最终利润率
    xxAdditionalQuote: null,//欣象其他费用
    softWareExtQuoteTotal: null,//软件建议对外报价总和
    hardWareExtQuoteTotal: null,//硬件建议对外报价总和
    implementationExtQuoteTotal: null,//实施建议对外报价总和
    softWareCostTotal: null,  //软件费用小计-成本
    hardWareCostTotal: null,  //硬件费用小计-成本
    xxSoftWareQuoteTotal: null,//欣象软件报价小计
    xxHardWareQuoteTotal: null,//欣象硬件报价小计
    projCostTotal: null,  // 项目成本合计 
    projSugQuoteTotal: null,  // 项目建议报价合计
    projExtQuoteTotal: null,//项目对外报价
    projProfitRateExcl: null,  // 项目利润率(不包含外采硬件)
    projProfitRateIncl: null,// 项目利润率(含外采硬件) 
    signType: '1' //签约类型
})
const signType = ref('1')
const quoteType = ref('cost');
const isDocumentaryModellVisible = ref(false) //利润低于15%
const loading = ref(true);
const isQuoteDisabled = computed(() => props.opportunity.type === 'INCAPABLE');
const quoteDesc = ref('')
const closeNote = ref('') // 继续跟单理由
const description = ref('') //销售粗略报价情况说明
const unableQuoteAmount = ref('') //销售粗略报价--北光
const xxUnableQuoteAmount = ref('')
const externalProfit = ref(null) //预警  合同签约最低利润率
const comTitle = ref('')
const currentVersion = ref('')
const isInitialLoad = ref(true);// 标识初始化加载状态 ---用于刚进入此弹窗的时候有关产品和硬件相关的建议报价的计算
watch(() => props.opportunity, (newVal) => {
    if (newVal) {
        quoteDesc.value = newVal.quoteDesc
        if (newVal.type === 'INCAPABLE') {
            comTitle.value = '售前无法报价'
        } else if (newVal.type === 'ROUGH') {
            comTitle.value = '售前粗略报价'
        } else if (newVal.type === 'COST') {
            comTitle.value = '售前详细报价'
        }
    }
}, { immediate: true });
watch(() => props.isSales, (newVal) => {
    quoteDesc.value = props.opportunity.quoteDesc
    // 调取预警接口
    getProfitFunc()
    if (newVal) {
        if (props.isUpdateSalesOffer) {
            if (props.opportunity.type === 'INCAPABLE') {
                quoteType.value = 'upload';
            }
            getSalesInfoFunc(fetchReSalesInfo, true)
        } else {
            // 销售报价调取接口获取成本数据 根据成本数据 销售报价  
            if (props.opportunity.type === 'COST') {
                getSalesInfoFunc(fetchSalesDetailInfo);
            } else if (props.opportunity.type === 'ROUGH') {
                getSalesInfoFunc(fetchSalesRoughInfo);
            } else if (props.opportunity.type === 'INCAPABLE') {
                quoteType.value = 'upload';
                loading.value = false
            }
        }

    } else {
        console.log(newVal);
    }
});
// 计算属性判断是否高亮
const isProfitRateLow = computed(() => {
    if (props.isQuoteDetail) {
        return (quoteType.value === 'cost' &&
            quotesPresaleInfo?.value.projProfitRateExcl < externalProfit.value)
    } else {
        return (quoteType.value === 'cost' &&
            projProfitRateExcl?.value < externalProfit.value);
    }
});
// 查看销售报价详情
watch(() => props.isQuoteDetail, (newVal) => {
    quoteDesc.value = props.opportunity.quoteDesc
    if (newVal) {
        if (props.isRoughSalesQuote) {
            quoteType.value = 'upload';
        } else {
            quoteType.value = 'cost';
        }
        // 调取接口获取销售报价详情
        console.log(newVal, '销售报价详情')
        getSalesDetailInfoFunc()
    } else {
        console.log(newVal)
    }
});

// 确定用户已经输入过内容
const inputPreProfit = ref(false)
const inputDevProfit = ref(false)
const inputProdProfit = ref(false)
const inputOtherProfit = ref(false)
const inputSelfProfit = ref(false)
const inputExtProfit = ref(false)
const inputImpProfit = ref(false)
const handleProfit = (v) => {
    if (v === 'pre') {
        inputPreProfit.value = true
    }
    if (v === 'dev') {
        inputDevProfit.value = true
    }
    if (v === 'prod') {
        inputProdProfit.value = true
    }
    if (v === 'other') {
        inputOtherProfit.value = true
    }
    if (v === 'self') {
        inputSelfProfit.value = true
    }
    if (v === 'ext') {
        inputExtProfit.value = true
    }
    if (v === 'imp') {
        inputImpProfit.value = true
    }
}

// 售前-----------------------------------
// 售前利润率---输入的是百分比形式要转化成小数形式
const preProfitRate = ref(null)
watch(() => preProfitRate.value, (newVal) => {
    const preProfitRates = newVal / 100
    quotesPresaleInfo.value.preProfitRate = preProfitRates
    if (inputPreProfit.value) {
        quotesPresaleInfo.value.preSugQuote = calculatePreSugQuote.value;
    }
})


// 确定用户已经输入过内容
const inputXXPerChanged = ref(false)
const inputPerChanged = ref(false)
const onUserPerInput = (amount) => {
    inputPerChanged.value = true
    quotesPresaleInfo.value.xxPreQuote = Number(amount) / 0.97
}
// 计算建议报价  
const calculatePreSugQuote = computed(() => {
    return quotesPresaleInfo.value.prePrice * (1 + quotesPresaleInfo.value.preTaxRate) * (1 + quotesPresaleInfo.value.preProfitRate);
});

// 更新建议报价  
watch(() => quotesPresaleInfo.value.preProfitRate, () => {
    if (inputPreProfit.value) {
        quotesPresaleInfo.value.preSugQuote = calculatePreSugQuote.value;
    }
    if (!props.isQuoteDetail && !props.isUpdateSalesOffer) {
        if (!inputPerChanged.value && !inputXXPerChanged.value) {
            quotesPresaleInfo.value.preExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.preSugQuote))
            quotesPresaleInfo.value.xxPreQuote = formatIntNumber(Number(quotesPresaleInfo.value.preSugQuote) / 0.97)
        }
    }

});
// 计算软件费用小计-对外报价  
const softWareRefQuoteTotal = computed(() => {
    return (
        formatIntNumber(parseFloat(quotesPresaleInfo.value.preSugQuote || 0)) +
        formatIntNumber(parseFloat(quotesPresaleInfo.value.devSugQuote || 0)) +
        formatIntNumber(parseFloat(quotesPresaleInfo.value.prodSugQuote || 0)) +
        formatIntNumber(parseFloat(quotesPresaleInfo.value.otherSugQuote || 0))
    );
});

// 计算硬件费用小计-对外报价  
const hardWareRefQuoteTotal = computed(() => {
    return (
        formatIntNumber(parseFloat(quotesPresaleInfo.value.selfSugQuote || 0)) +
        formatIntNumber(parseFloat(quotesPresaleInfo.value.extSugQuote || 0))
    );
});

// 计算实施费用小计-对外报价  
const implementationRefQuoteTotal = computed(() => {
    return formatIntNumber(parseFloat(quotesPresaleInfo.value.impSugQuote || 0));
});

// 定制开发-----------------------------------
// 售前利润率---输入的是百分比形式要转化成小数形式
const devProfitRate = ref(null)
watch(() => devProfitRate.value, (newVal) => {
    const devProfitRates = newVal / 100
    quotesPresaleInfo.value.devProfitRate = devProfitRates
    if (inputDevProfit.value) {
        quotesPresaleInfo.value.devSugQuote = calculateDevSugQuote.value;
    }
})
// 确定用户已经输入过内容
const inputXXDevChanged = ref(false)
const inputDevChanged = ref(false)
const onUserDevInput = (amount) => {
    inputDevChanged.value = true
    quotesPresaleInfo.value.xxDevQuote = Number(amount) / 0.97
}
// 计算建议报价  
const calculateDevSugQuote = computed(() => {
    return quotesPresaleInfo.value.devPrice * (1 + quotesPresaleInfo.value.devTaxRate) * (1 + quotesPresaleInfo.value.devProfitRate);
});

// 更新建议报价  
watch(() => quotesPresaleInfo.value.devProfitRate, () => {
    if (inputDevProfit.value) {
        quotesPresaleInfo.value.devSugQuote = calculateDevSugQuote.value;
    }
    if (!props.isQuoteDetail && !props.isUpdateSalesOffer) {
        if (!inputDevChanged.value && !inputXXDevChanged.value) {
            quotesPresaleInfo.value.devExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.devSugQuote))
            quotesPresaleInfo.value.xxDevQuote = formatIntNumber(Number(quotesPresaleInfo.value.devSugQuote) / 0.97)
        }
    }
});
// 产品-----------------------------------
// 售前利润率---输入的是百分比形式要转化成小数形式
const prodProfitRate = ref(null)
watch(() => prodProfitRate.value, (newVal) => {
    const prodProfitRate = newVal / 100
    quotesPresaleInfo.value.prodProfitRate = prodProfitRate
    console.log('cost1')
    if (inputProdProfit.value) {
        if (props.opportunity.type !== 'COST') {
            console.log('cost2')
            quotesPresaleInfo.value.prodSugQuote = calculateProdSugQuote.value;
        }
    }

})
// 确定用户已经输入过内容
const inputXXProdChanged = ref(false)
const inputProdChanged = ref(false)
const onUserProdInput = (amount) => {
    inputProdChanged.value = true
    quotesPresaleInfo.value.xxProdQuote = Number(amount) / 0.97

}
// 计算建议报价  
const calculateProdSugQuote = computed(() => {
    return quotesPresaleInfo.value.prodPrice * (1 + quotesPresaleInfo.value.prodTaxRate) * (1 + quotesPresaleInfo.value.prodProfitRate);
});

// 更新建议报价  
watch(() => quotesPresaleInfo.value.prodProfitRate, () => {
    if (inputProdProfit.value) {
        // if (props.opportunity.type !== 'COST') {    
        quotesPresaleInfo.value.prodSugQuote = calculateProdSugQuote.value;
    }
    // }
    if (!props.isQuoteDetail && !props.isUpdateSalesOffer) {
        if (!inputProdChanged.value && !inputXXProdChanged.value) {
            if (props.opportunity.type !== 'COST') {
                quotesPresaleInfo.value.prodExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.prodSugQuote))
                quotesPresaleInfo.value.xxProdQuote = formatIntNumber(Number(quotesPresaleInfo.value.prodSugQuote) / 0.97)

            }
        }
    }
});
// 其他-----------------------------------
// 售前利润率---输入的是百分比形式要转化成小数形式
const otherProfitRate = ref(null)
watch(() => otherProfitRate.value, (newVal) => {
    const otherProfitRate = newVal / 100
    quotesPresaleInfo.value.otherProfitRate = otherProfitRate
    if (inputOtherProfit.value) {
        quotesPresaleInfo.value.otherSugQuote = calculateOtherSugQuote.value;
    }
})
// 确定用户已经输入过内容
const inputXXOtherChanged = ref(false)

const inputOtherChanged = ref(false)
const onUserOtherInput = (amount) => {
    inputOtherChanged.value = true
    quotesPresaleInfo.value.xxOtherQuote = Number(amount) / 0.97

}
// 计算建议报价  
const calculateOtherSugQuote = computed(() => {
    return quotesPresaleInfo.value.otherPrice * (1 + quotesPresaleInfo.value.otherTaxRate) * (1 + quotesPresaleInfo.value.otherProfitRate);
});

// 更新建议报价  
watch(() => quotesPresaleInfo.value.otherProfitRate, () => {
    if (inputOtherProfit.value) {
        quotesPresaleInfo.value.otherSugQuote = calculateOtherSugQuote.value;
    }
    if (!props.isQuoteDetail && !props.isUpdateSalesOffer) {
        if (!inputOtherChanged.value && !inputXXOtherChanged.value) {
            quotesPresaleInfo.value.otherExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.otherSugQuote))
            quotesPresaleInfo.value.xxOtherQuote = formatIntNumber(Number(quotesPresaleInfo.value.otherSugQuote) / 0.97)
        }
    }
});
// 硬件自研-----------------------------------
// 售前利润率---输入的是百分比形式要转化成小数形式
const selfProfitRate = ref(null)
watch(() => selfProfitRate.value, (newVal) => {
    const selfProfitRate = newVal / 100
    quotesPresaleInfo.value.selfProfitRate = selfProfitRate
    if (inputSelfProfit.value) {
        if (props.opportunity.type !== 'COST') {
            quotesPresaleInfo.value.selfSugQuote = calculateSelfSugQuote.value;
        }
    }
})
// 确定用户已经输入过内容
const inputXXSelfChanged = ref(false)

const inputSelfChanged = ref(false)
const onUserSelfInput = (amount) => {
    inputSelfChanged.value = true
    quotesPresaleInfo.value.xxSelfQuote = Number(amount) / 0.97
}
// 计算建议报价  
const calculateSelfSugQuote = computed(() => {
    return quotesPresaleInfo.value.selfPrice * (1 + quotesPresaleInfo.value.selfTaxRate) * (1 + quotesPresaleInfo.value.selfProfitRate);
});

// 更新建议报价  
watch(() => quotesPresaleInfo.value.selfProfitRate, () => {
    if (inputSelfProfit.value) {
        quotesPresaleInfo.value.selfSugQuote = calculateSelfSugQuote.value;
    }
    if (!props.isQuoteDetail && !props.isUpdateSalesOffer) {
        if (!inputSelfChanged.value && !inputXXSelfChanged.value) {
            quotesPresaleInfo.value.selfExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.selfSugQuote))
            quotesPresaleInfo.value.xxSelfQuote = formatIntNumber(Number(quotesPresaleInfo.value.selfSugQuote) / 0.97)
        }
    }
});
// 硬件采购-----------------------------------
// 售前利润率---输入的是百分比形式要转化成小数形式
const extProfitRate = ref(null)
watch(() => extProfitRate.value, (newVal) => {
    const extProfitRate = newVal / 100
    quotesPresaleInfo.value.extProfitRate = extProfitRate
    // if (props.opportunity.type !== 'COST') {    
    if (inputExtProfit.value) {
        quotesPresaleInfo.value.extSugQuote = calculateExtSugQuote.value
    }
    // }
})
// 确定用户已经输入过内容
const inputXXExtChanged = ref(false)

const inputExtChanged = ref(false)
const onUserExtInput = (amount) => {
    inputExtChanged.value = true
    quotesPresaleInfo.value.xxExtQuote = Number(amount)

}
// 计算建议报价  
const calculateExtSugQuote = computed(() => {
    return quotesPresaleInfo.value.extPrice * (1 + quotesPresaleInfo.value.extTaxRate) * (1 + quotesPresaleInfo.value.extProfitRate);
});

// 更新建议报价  
watch(() => quotesPresaleInfo.value.extProfitRate, () => {
    if (inputExtProfit.value) {
        quotesPresaleInfo.value.extSugQuote = calculateExtSugQuote.value;
    }
    if (!props.isQuoteDetail && !props.isUpdateSalesOffer) {
        if (!inputExtChanged.value && !inputXXExtChanged.value) {
            quotesPresaleInfo.value.extExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.extSugQuote))
            quotesPresaleInfo.value.xxExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.extSugQuote))
        }
    }
});
// 实施-----------------------------------
// 输入的是百分比形式要转化成小数形式
const impProfitRate = ref(null)
watch(() => impProfitRate.value, (newVal) => {
    const impProfitRate = newVal / 100
    quotesPresaleInfo.value.impProfitRate = impProfitRate
    if (inputImpProfit.value) {
        quotesPresaleInfo.value.impSugQuote = calculateImpSugQuote.value;
    }
})
// 确定用户已经输入过内容
const inputXXImpChanged = ref(false)

const inputImpChanged = ref(false)
const onUserImpInput = (amount) => {
    inputImpChanged.value = true
    quotesPresaleInfo.value.xxImpQuote = Number(amount) / 0.97
}
// 计算建议报价  
const calculateImpSugQuote = computed(() => {
    return quotesPresaleInfo.value.impPrice * (1 + quotesPresaleInfo.value.impTaxRate) * (1 + quotesPresaleInfo.value.impProfitRate);
});

// 更新建议报价  
watch(() => quotesPresaleInfo.value.impProfitRate, () => {
    if (inputImpProfit.value) {
        quotesPresaleInfo.value.impSugQuote = calculateImpSugQuote.value;
    }
    if (!props.isQuoteDetail && !props.isUpdateSalesOffer) {
        if (!inputImpChanged.value && !inputXXImpChanged.value) {
            quotesPresaleInfo.value.impExtQuote = formatIntNumber(Number(quotesPresaleInfo.value.impSugQuote))
            quotesPresaleInfo.value.xxImpQuote = formatIntNumber(Number(quotesPresaleInfo.value.impSugQuote) / 0.97)
        }
    }
});
// 计算计算软件费用小计-欣象对外报价
const xxSoftWareQuoteTotal = computed(() => {
    return (
        parseFloat(quotesPresaleInfo.value.xxPreQuote || 0) +
        parseFloat(quotesPresaleInfo.value.xxDevQuote || 0) +
        parseFloat(quotesPresaleInfo.value.xxProdQuote || 0) +
        parseFloat(quotesPresaleInfo.value.xxOtherQuote || 0) +
        parseFloat(quotesPresaleInfo.value.xxExtProxyQuote || 0)
    );
});
// 计算硬件费用小计-欣象对外报价  
const xxHardWareQuoteTotal = computed(() => {
    return (
        parseFloat(quotesPresaleInfo.value.xxSelfQuote || 0) +
        parseFloat(quotesPresaleInfo.value.xxExtQuote || 0)
    );
});
// 计算软件费用小计-对外报价  
const softWareExtQuoteTotal = computed(() => {
    return (
        parseFloat(quotesPresaleInfo.value.preExtQuote || 0) +
        parseFloat(quotesPresaleInfo.value.devExtQuote || 0) +
        parseFloat(quotesPresaleInfo.value.prodExtQuote || 0) +
        parseFloat(quotesPresaleInfo.value.otherExtQuote || 0)
    );
});

// 计算硬件费用小计-对外报价  
const hardWareExtQuoteTotal = computed(() => {
    return (
        parseFloat(quotesPresaleInfo.value.selfExtQuote || 0) +
        parseFloat(quotesPresaleInfo.value.extExtQuote || 0)
    );
});

// 计算实施费用小计-对外报价  
const implementationExtQuoteTotal = computed(() => {
    return parseFloat(quotesPresaleInfo.value.impExtQuote || 0);
});
// 计算欣象/北光报价转换
const updateNorthQuote = () => {
    // 计算北光报价，并更新绑定变量  
    if (xxUnableQuoteAmount.value) {
        const isNumeric = !isNaN(xxUnableQuoteAmount.value);
        unableQuoteAmount.value = isNumeric
            ? (parseFloat(xxUnableQuoteAmount.value) * 0.97) // 四舍五入到小数点后两位  
            : ''; // 如果输入不是数字，则清空北光报价  
    } else {
        unableQuoteAmount.value = ''; // 如果清空欣象报价，则清空北光报价 
    }
}
const preventDecimal = (event) => {
    // 检查按键，阻止小数点的输入  
    if (event.key === '.') {
        event.preventDefault();
        message.warning({
            content: () => '不允许输入小数',
            class: 'custom-class',
            style: {
                marginTop: '50vh',
            },
            duration: 3,
        })
    }
}

const updateNorthLightQuote = (type, xxQuoteValue) => {
    let referenceQuote;
    switch (type) {
        case 'pre':
            referenceQuote = quotesPresaleInfo.value.preSugQuote;
            quotesPresaleInfo.value.preExtQuote = (xxQuoteValue * 0.97);
            break;
        case 'dev':
            referenceQuote = quotesPresaleInfo.value.devSugQuote;
            quotesPresaleInfo.value.devExtQuote = (xxQuoteValue * 0.97);
            break;
        case 'prod':
            referenceQuote = quotesPresaleInfo.value.prodSugQuote;
            quotesPresaleInfo.value.prodExtQuote = (xxQuoteValue * 0.97);
            break;
        case 'other':
            referenceQuote = quotesPresaleInfo.value.otherSugQuote;
            quotesPresaleInfo.value.otherExtQuote = (xxQuoteValue * 0.97);
            break;
        case 'self':
            referenceQuote = quotesPresaleInfo.value.selfSugQuote;
            quotesPresaleInfo.value.selfExtQuote = (xxQuoteValue * 0.97);
            break;
        case 'ext':
            referenceQuote = quotesPresaleInfo.value.extSugQuote;
            quotesPresaleInfo.value.extExtQuote = xxQuoteValue
            break;
        case 'imp':
            referenceQuote = quotesPresaleInfo.value.impSugQuote;
            quotesPresaleInfo.value.impExtQuote = (xxQuoteValue * 0.97);
            break;
    }

    if (type === 'pre') {
        inputXXPerChanged.value = true
        quotesPresaleInfo.value.preExtQuote = quotesPresaleInfo.value.preExtQuote >= 0 ? quotesPresaleInfo.value.preExtQuote : referenceQuote;
    } else if (type === 'dev') {
        inputXXDevChanged.value = true
        quotesPresaleInfo.value.devExtQuote = quotesPresaleInfo.value.devExtQuote >= 0 ? quotesPresaleInfo.value.devExtQuote : referenceQuote;
    } else if (type === 'prod') {
        inputXXProdChanged.value = true
        quotesPresaleInfo.value.prodExtQuote = quotesPresaleInfo.value.prodExtQuote >= 0 ? quotesPresaleInfo.value.prodExtQuote : referenceQuote;
    } else if (type === 'other') {
        inputXXOtherChanged.value = true
        quotesPresaleInfo.value.otherExtQuote = quotesPresaleInfo.value.otherExtQuote >= 0 ? quotesPresaleInfo.value.otherExtQuote : referenceQuote;
    } else if (type === 'self') {
        inputXXSelfChanged.value = true
        quotesPresaleInfo.value.selfExtQuote = quotesPresaleInfo.value.selfExtQuote >= 0 ? quotesPresaleInfo.value.selfExtQuote : referenceQuote;
    } else if (type === 'ext') {
        inputXXExtChanged.value = true
        quotesPresaleInfo.value.extExtQuote = quotesPresaleInfo.value.extExtQuote >= 0 ? quotesPresaleInfo.value.extExtQuote : referenceQuote;
    } else if (type === 'imp') {
        inputXXImpChanged.value = true
        quotesPresaleInfo.value.impExtQuote = quotesPresaleInfo.value.impExtQuote >= 0 ? quotesPresaleInfo.value.impExtQuote : referenceQuote;
    }
};
// 计算对外报价合计 
const proXxAllExtQuoteTotal = computed(() => {
    const softWareTotal = Number(xxSoftWareQuoteTotal.value) || 0;
    const hardWareTotal = Number(xxHardWareQuoteTotal.value) || 0;
    const implementationTotal = Number(quotesPresaleInfo.value.xxImpQuote) || 0;
    const otherTotal = Number(quotesPresaleInfo.value.xxAdditionalQuote) || 0

    return (softWareTotal + hardWareTotal + implementationTotal + otherTotal);
});
const proBgAllExtQuoteTotal = computed(() => {
    const softWareTotal = Number(softWareExtQuoteTotal.value) || 0;
    const hardWareTotal = Number(hardWareExtQuoteTotal.value) || 0;
    const implementationTotal = Number(implementationExtQuoteTotal.value) || 0;
    return (softWareTotal + hardWareTotal + implementationTotal);
});
// 计算项目对外报价总计   
const projExtQuoteTotal = computed(() => {
    const softWareTotal = Number(softWareExtQuoteTotal.value) || 0;
    const hardWareTotal = Number(hardWareExtQuoteTotal.value) || 0;
    const implementationTotal = Number(implementationExtQuoteTotal.value) || 0;
    return (softWareTotal + hardWareTotal + implementationTotal);
});
watch(() => softWareExtQuoteTotal.value, (newValue) => {
    quotesPresaleInfo.value.softWareExtQuoteTotal = newValue;
});
watch(() => hardWareExtQuoteTotal.value, (newValue) => {
    quotesPresaleInfo.value.hardWareExtQuoteTotal = newValue;
});
watch(() => implementationExtQuoteTotal.value, (newValue) => {
    quotesPresaleInfo.value.implementationExtQuoteTotal = newValue;
});
// 计算项目利润率（不含外采硬件）
const projProfitRateExcl = computed(() => {
    const totalCost = parseFloat(quotesPresaleInfo.value.softWareCostTotal || 0) + parseFloat(quotesPresaleInfo.value.selfPrice || 0) + parseFloat(quotesPresaleInfo.value.impPrice || 0);

    if (totalCost === 0 && quotesPresaleInfo.value.projCostTotal === 0) {
        return 0;  // 如果总成本为0，显示 "-"
    } else {
        // 计算利润率公式
        // const softwareProfit = (parseFloat(softWareExtQuoteTotal.value || 0) / 1.06 - parseFloat(quotesPresaleInfo.value.softWareCostTotal || 0));
        // const selfProfit = (parseFloat(quotesPresaleInfo.value.selfExtQuote || 0) / 1.13 - parseFloat(quotesPresaleInfo.value.selfPrice || 0));
        // const impProfit = (parseFloat(quotesPresaleInfo.value.impExtQuote || 0) / 1.06 - parseFloat(quotesPresaleInfo.value.impPrice || 0));
        const softwareProfit =
            (parseFloat(quotesPresaleInfo.value.preExtQuote || 0) / (1 + quotesPresaleInfo.value.preTaxRate) - parseFloat(quotesPresaleInfo.value.prePrice || 0)) +
            (parseFloat(quotesPresaleInfo.value.devExtQuote || 0) / (1 + quotesPresaleInfo.value.devTaxRate) - parseFloat(quotesPresaleInfo.value.devPrice || 0)) +
            (parseFloat(quotesPresaleInfo.value.prodExtQuote || 0) / (1 + quotesPresaleInfo.value.prodTaxRate) - parseFloat(quotesPresaleInfo.value.prodPrice || 0)) +
            (parseFloat(quotesPresaleInfo.value.otherExtQuote || 0) / (1 + quotesPresaleInfo.value.otherTaxRate) - parseFloat(quotesPresaleInfo.value.otherPrice || 0))

        const selfProfit = (parseFloat(quotesPresaleInfo.value.selfExtQuote || 0) / (1 + quotesPresaleInfo.value.selfTaxRate) - parseFloat(quotesPresaleInfo.value.selfPrice || 0));
        const impProfit = (parseFloat(quotesPresaleInfo.value.impExtQuote || 0) / (1 + quotesPresaleInfo.value.impTaxRate) - parseFloat(quotesPresaleInfo.value.impPrice || 0));
        const totalProfit = softwareProfit + selfProfit + impProfit;
        const profitRate = ref(null)  // 计算利润率
        if (totalCost !== 0) {
            profitRate.value = totalProfit / totalCost;  // 计算利润率
        } else {
            profitRate.value = (projExtQuoteTotal.value / 1.13 - quotesPresaleInfo.value.projCostTotal) / quotesPresaleInfo.value.projCostTotal;  // 计算利润率

        }
        console.log(profitRate, totalCost, softwareProfit, selfProfit, impProfit, '-------')
        return profitRate.value;
    }
})
// 计算项目利润率（含外采硬件）
const projProfitRateIncl = computed(() => {
    const totalCost = parseFloat(quotesPresaleInfo.value.softWareCostTotal || 0) + parseFloat(quotesPresaleInfo.value.selfPrice || 0) + parseFloat(quotesPresaleInfo.value.impPrice || 0);
    // 如果项目成本合计为0，则显示"-"
    if (quotesPresaleInfo.value.projCostTotal === 0) {
        return 0;
    }

    // // 计算软件费用利润
    // const softwareProfit = (quotesPresaleInfo.value.softWareExtQuoteTotal / 1.06 - quotesPresaleInfo.value.softWareCostTotal);
    // // 计算硬件费用利润
    // const hardwareProfit = (quotesPresaleInfo.value.hardWareExtQuoteTotal / 1.13 - quotesPresaleInfo.value.hardWareCostTotal);
    // // 计算实施费用利润
    // const implementationProfit = (quotesPresaleInfo.value.implementationExtQuoteTotal / 1.06 - quotesPresaleInfo.value.impPrice);
    const softwareProfit =
        (parseFloat(quotesPresaleInfo.value.preExtQuote || 0) / (1 + quotesPresaleInfo.value.preTaxRate) - parseFloat(quotesPresaleInfo.value.prePrice || 0)) +
        (parseFloat(quotesPresaleInfo.value.devExtQuote || 0) / (1 + quotesPresaleInfo.value.devTaxRate) - parseFloat(quotesPresaleInfo.value.devPrice || 0)) +
        (parseFloat(quotesPresaleInfo.value.prodExtQuote || 0) / (1 + quotesPresaleInfo.value.prodTaxRate) - parseFloat(quotesPresaleInfo.value.prodPrice || 0)) +
        (parseFloat(quotesPresaleInfo.value.otherExtQuote || 0) / (1 + quotesPresaleInfo.value.otherTaxRate) - parseFloat(quotesPresaleInfo.value.otherPrice || 0))

    const hardwareProfit =
        (parseFloat(quotesPresaleInfo.value.selfExtQuote || 0) / (1 + quotesPresaleInfo.value.selfTaxRate) - parseFloat(quotesPresaleInfo.value.selfPrice || 0)) +
        (parseFloat(quotesPresaleInfo.value.extExtQuote || 0) / (1 + quotesPresaleInfo.value.extTaxRate) - parseFloat(quotesPresaleInfo.value.extPrice || 0))
    const implementationProfit = (parseFloat(quotesPresaleInfo.value.impExtQuote || 0) / (1 + quotesPresaleInfo.value.impTaxRate) - parseFloat(quotesPresaleInfo.value.impPrice || 0));
    // 计算项目利润率
    const totalProfit = softwareProfit + hardwareProfit + implementationProfit;


    // 返回利润率
    if (totalCost === 0) {
        return projProfitRateExcl.value
    } else {
        return totalProfit / quotesPresaleInfo.value.projCostTotal;
    }
});
// 观察计算属性的变化  
watch(projProfitRateExcl, (newVal) => {
    if (!props.isQuoteDetail) {
        quotesPresaleInfo.value.projProfitRateExcl = newVal;
    }
});
watch(projProfitRateIncl, (newVal) => {
    if (!props.isQuoteDetail) {
        quotesPresaleInfo.value.projProfitRateIncl = newVal;
    }
});
watch(quotesPresaleInfo, (newVal) => {
    quotesPresaleInfo.value = newVal;
});

// 计算定制开发最终利润率  
const devActualProfitRate = computed(() => {
    const devPrice = quotesPresaleInfo.value.devPrice;
    const devExtQuote = formatIntNumber(quotesPresaleInfo.value.devExtQuote); 
    const devTaxRate = quotesPresaleInfo.value.devTaxRate;

    if (devPrice === 0 || devPrice === null) {
        return 0;
    } else {
        const calculatedRate = (devExtQuote / (1 + devTaxRate) - devPrice) / devPrice;
        return calculatedRate.toFixed(4);
    }
});
// 计算售前支持最终利润率  
const preActualProfitRate = computed(() => {
    const prePrice = quotesPresaleInfo.value.prePrice;
    const preExtQuote = formatIntNumber(quotesPresaleInfo.value.preExtQuote);
    const preTaxRate = quotesPresaleInfo.value.preTaxRate;

    if (prePrice === 0 || prePrice === null) {
        return '-';
    } else {
        const calculatedRate = (preExtQuote / (1 + preTaxRate) - prePrice) / prePrice;
        return calculatedRate.toFixed(4);
    }
});

// 计算产品最终利润率  
const prodActualProfitRate = computed(() => {
    const prodPrice = quotesPresaleInfo.value.prodPrice;
    const prodExtQuote = formatIntNumber(quotesPresaleInfo.value.prodExtQuote);
    const prodTaxRate = quotesPresaleInfo.value.prodTaxRate;

    if (prodPrice === 0 || prodPrice === null) {
        return '-';
    } else {
        const calculatedRate = (prodExtQuote / (1 + prodTaxRate) - prodPrice) / prodPrice;
        return calculatedRate.toFixed(4);
    }
});
// 计算其他最终利润率  
const otherActualProfitRate = computed(() => {
    const otherPrice = quotesPresaleInfo.value.otherPrice;
    const otherExtQuote = formatIntNumber(quotesPresaleInfo.value.otherExtQuote);
    const otherTaxRate = quotesPresaleInfo.value.otherTaxRate;

    if (otherPrice === 0 || otherPrice === null) {
        return '-';
    } else {
        const calculatedRate = (otherExtQuote / (1 + otherTaxRate) - otherPrice) / otherPrice;
        return calculatedRate.toFixed(4);
    }
});
// 计算自研最终利润率  
const selfActualProfitRate = computed(() => {
    const selfPrice = quotesPresaleInfo.value.selfPrice;
    const selfExtQuote = formatIntNumber(quotesPresaleInfo.value.selfExtQuote);
    const selfTaxRate = quotesPresaleInfo.value.selfTaxRate;

    if (selfPrice === 0 || selfPrice === null) {
        return '-';  // 如果自研价格为0，则显示-  
    } else {
        const calculatedRate = (selfExtQuote / (1 + selfTaxRate) - selfPrice) / selfPrice;
        return calculatedRate.toFixed(4); // 格式化为四位小数  
    }
});
// 计算外采最终利润率  
const extActualProfitRate = computed(() => {
    const extPrice = quotesPresaleInfo.value.extPrice;
    const extExtQuote = formatIntNumber(quotesPresaleInfo.value.extExtQuote);
    const extTaxRate = quotesPresaleInfo.value.extTaxRate;

    if (extPrice === 0 || extPrice === null) {
        return '-';  // 如果外采价格为0，则显示-  
    } else {
        const calculatedRate = (extExtQuote / (1 + extTaxRate) - extPrice) / extPrice;
        return calculatedRate.toFixed(4); // 格式化为四位小数  
    }
});
// 计算实施最终利润率  
const impActualProfitRate = computed(() => {
    const impPrice = quotesPresaleInfo.value.impPrice;
    const impExtQuote = formatIntNumber(quotesPresaleInfo.value.impExtQuote);
    const impTaxRate = quotesPresaleInfo.value.impTaxRate;

    if (impPrice === 0 || impPrice === null) {
        return '-';
    } else {
        const calculatedRate = (impExtQuote / (1 + impTaxRate) - impPrice) / impPrice;
        return calculatedRate.toFixed(4);
    }
});

// 观察计算属性的变化，并更新 quotesPresaleInfo 的相应字段  
watch(devActualProfitRate, (newValue) => {
    quotesPresaleInfo.value.devActualProfitRate = newValue;
});
watch(impActualProfitRate, (newValue) => {
    quotesPresaleInfo.value.impActualProfitRate = newValue;
});

watch(otherActualProfitRate, (newValue) => {
    quotesPresaleInfo.value.otherActualProfitRate = newValue;
});

watch(preActualProfitRate, (newValue) => {
    quotesPresaleInfo.value.preActualProfitRate = newValue;
});

watch(prodActualProfitRate, (newValue) => {
    quotesPresaleInfo.value.prodActualProfitRate = newValue;
});
watch(selfActualProfitRate, (newValue) => {
    quotesPresaleInfo.value.selfActualProfitRate = newValue;
});

watch(extActualProfitRate, (newValue) => {
    quotesPresaleInfo.value.extActualProfitRate = newValue;
});
watch(signType, (newValue) => {
    quotesPresaleInfo.value.signType = newValue;
});
const getProfitFunc = async () => {
    try {
        const response = await getProfit();
        if (response && response.code === 200) {
            externalProfit.value = response.data.externalProfit
            console.log(externalProfit.value, 'externalProfit.value')
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

const getSalesInfoFunc = async (fetchFunc, boolean) => {
    const quoteType = boolean ? '' : signType.value;
    try {
        const response = await fetchFunc({ opportunitiesId: props.opportunity.id, quoteType: quoteType });
        if (response && response.code === 200) {
            const data = response.data
            if (data.quoteDesc) quoteDesc.value = data.quoteDesc
            if (data.closeNote) closeNote.value = data.closeNote
            quotesPresaleInfo.value.opportunitiesId = data.id
            quotesPresaleInfo.value.devPrice = data.quotesPresaleInfo.devPrice;
            quotesPresaleInfo.value.devTaxRate = Number(data.quotesPresaleInfo.devTaxRate);
            quotesPresaleInfo.value.devProfitRate = Number(data.quotesPresaleInfo.devProfitRate);
            quotesPresaleInfo.value.devSugQuote = data.quotesPresaleInfo.devSugQuote;
            quotesPresaleInfo.value.xxDevQuote = data.quotesPresaleInfo.xxDevQuote;
            quotesPresaleInfo.value.devExtQuote = data.quotesPresaleInfo.devExtQuote;
            devProfitRate.value = Number(data.quotesPresaleInfo.devProfitRate) * 100 //利润率计算


            quotesPresaleInfo.value.extPrice = data.quotesPresaleInfo.extPrice;
            quotesPresaleInfo.value.extTaxRate = Number(data.quotesPresaleInfo.extTaxRate);
            quotesPresaleInfo.value.extProfitRate = Number(data.quotesPresaleInfo.extProfitRate);
            quotesPresaleInfo.value.extSugQuote = data.quotesPresaleInfo.extSugQuote;
            quotesPresaleInfo.value.xxExtQuote = data.quotesPresaleInfo.xxExtQuote;
            quotesPresaleInfo.value.extExtQuote = data.quotesPresaleInfo.extExtQuote;
            extProfitRate.value = Number(data.quotesPresaleInfo.extProfitRate) * 100 //利润率计算

            quotesPresaleInfo.value.impPrice = data.quotesPresaleInfo.impPrice
            quotesPresaleInfo.value.impTaxRate = Number(data.quotesPresaleInfo.impTaxRate);
            quotesPresaleInfo.value.impProfitRate = Number(data.quotesPresaleInfo.impProfitRate);
            quotesPresaleInfo.value.impSugQuote = data.quotesPresaleInfo.impSugQuote;
            quotesPresaleInfo.value.xxImpQuote = data.quotesPresaleInfo.xxImpQuote;
            quotesPresaleInfo.value.impExtQuote = data.quotesPresaleInfo.impExtQuote;
            impProfitRate.value = Number(data.quotesPresaleInfo.impProfitRate) * 100 //利润率计算

            quotesPresaleInfo.value.otherPrice = data.quotesPresaleInfo.otherPrice
            quotesPresaleInfo.value.otherTaxRate = Number(data.quotesPresaleInfo.otherTaxRate);
            quotesPresaleInfo.value.otherProfitRate = Number(data.quotesPresaleInfo.otherProfitRate);
            quotesPresaleInfo.value.otherSugQuote = data.quotesPresaleInfo.otherSugQuote;
            quotesPresaleInfo.value.xxOtherQuote = data.quotesPresaleInfo.xxOtherQuote;
            quotesPresaleInfo.value.otherExtQuote = data.quotesPresaleInfo.otherExtQuote
            otherProfitRate.value = Number(data.quotesPresaleInfo.otherProfitRate) * 100 //利润率计算

            quotesPresaleInfo.value.xxExtProxyQuote = data.quotesPresaleInfo.xxExtProxyQuote

            quotesPresaleInfo.value.prePrice = data.quotesPresaleInfo.prePrice
            quotesPresaleInfo.value.preTaxRate = Number(data.quotesPresaleInfo.preTaxRate);
            quotesPresaleInfo.value.preProfitRate = Number(data.quotesPresaleInfo.preProfitRate);
            quotesPresaleInfo.value.preSugQuote = data.quotesPresaleInfo.preSugQuote;
            quotesPresaleInfo.value.xxPreQuote = data.quotesPresaleInfo.xxPreQuote;
            quotesPresaleInfo.value.preExtQuote = data.quotesPresaleInfo.preExtQuote;
            preProfitRate.value = Number(data.quotesPresaleInfo.preProfitRate) * 100 //利润率计算


            quotesPresaleInfo.value.prodPrice = data.quotesPresaleInfo.prodPrice;
            quotesPresaleInfo.value.prodTaxRate = Number(data.quotesPresaleInfo.prodTaxRate);
            quotesPresaleInfo.value.prodProfitRate = Number(data.quotesPresaleInfo.prodProfitRate);
            quotesPresaleInfo.value.prodSugQuote = data.quotesPresaleInfo.prodSugQuote;
            quotesPresaleInfo.value.xxProdQuote = data.quotesPresaleInfo.xxProdQuote;
            quotesPresaleInfo.value.prodExtQuote = data.quotesPresaleInfo.prodExtQuote;
            prodProfitRate.value = Number(data.quotesPresaleInfo.prodProfitRate) * 100 //利润率计算

            quotesPresaleInfo.value.selfPrice = data.quotesPresaleInfo.selfPrice;
            quotesPresaleInfo.value.selfTaxRate = Number(data.quotesPresaleInfo.selfTaxRate);
            quotesPresaleInfo.value.selfProfitRate = Number(data.quotesPresaleInfo.selfProfitRate);
            quotesPresaleInfo.value.selfSugQuote = data.quotesPresaleInfo.selfSugQuote;
            quotesPresaleInfo.value.xxSelfQuote = data.quotesPresaleInfo.xxSelfQuote;
            quotesPresaleInfo.value.selfExtQuote = data.quotesPresaleInfo.selfExtQuote
            selfProfitRate.value = Number(data.quotesPresaleInfo.selfProfitRate) * 100 //利润率计算

            quotesPresaleInfo.value.xxAdditionalQuote = data.quotesPresaleInfo.xxAdditionalQuote

            // 小计数据  
            quotesPresaleInfo.value.softWareCostTotal = data.quotesPresaleInfo.softWareCostTotal;
            quotesPresaleInfo.value.hardWareCostTotal = data.quotesPresaleInfo.hardWareCostTotal;

            // 合计数据  
            quotesPresaleInfo.value.projCostTotal = data.quotesPresaleInfo.projCostTotal;
            quotesPresaleInfo.value.projSugQuoteTotal = data.quotesPresaleInfo.projSugQuoteTotal;
            quotesPresaleInfo.value.projProfitRateExcl = data.quotesPresaleInfo.projProfitRateExcl;
            quotesPresaleInfo.value.projProfitRateIncl = data.quotesPresaleInfo.projProfitRateIncl;

            if (props.isUpdateSalesOffer) {
                signType.value = data.quotesPresaleInfo.signType
                unableQuoteAmount.value = data.quotesPresaleInfo.unableQuoteAmount
                xxUnableQuoteAmount.value = data.quotesPresaleInfo.xxUnableQuoteAmount
            }
        } else {
            console.error('获取信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    } finally {
        isInitialLoad.value = false
        loading.value = false;
    }
};
const getSalesDetailInfoFunc = async () => {
    try {
        const response = await getSalesDetailInfoById({ id: props.selectedSalesId });
        if (response && response.code === 200) {
            const data = response.data
            if (data.type === 'INCAPABLE') {
                quoteType.value = 'upload';
            } else {
                quoteType.value = 'cost';
            }
            if (data.roughDesc) quoteDesc.value = data.roughDesc
            if (data.closeNote) closeNote.value = data.closeNote
            currentVersion.value = data.currentVersion
            quotesPresaleInfo.value.opportunitiesId = data.id
            quotesPresaleInfo.value.devPrice = data.devPrice;
            quotesPresaleInfo.value.devTaxRate = data.devTaxRate;
            quotesPresaleInfo.value.devProfitRate = data.devProfitRate;
            quotesPresaleInfo.value.xxDevQuote = data.xxDevQuote;
            quotesPresaleInfo.value.devSugQuote = data.devSugQuote;
            quotesPresaleInfo.value.devExtQuote = data.devExtQuote;


            quotesPresaleInfo.value.extPrice = data.extPrice;
            quotesPresaleInfo.value.extTaxRate = data.extTaxRate;
            quotesPresaleInfo.value.extProfitRate = data.extProfitRate;
            quotesPresaleInfo.value.xxExtQuote = data.xxExtQuote;
            quotesPresaleInfo.value.extSugQuote = data.extSugQuote;
            quotesPresaleInfo.value.extExtQuote = data.extExtQuote;

            quotesPresaleInfo.value.impPrice = data.impPrice;
            quotesPresaleInfo.value.impTaxRate = data.impTaxRate;
            quotesPresaleInfo.value.impProfitRate = data.impProfitRate;
            quotesPresaleInfo.value.xxImpQuote = data.xxImpQuote;
            quotesPresaleInfo.value.impSugQuote = data.impSugQuote;
            quotesPresaleInfo.value.impExtQuote = data.impExtQuote;

            quotesPresaleInfo.value.otherPrice = data.otherPrice;
            quotesPresaleInfo.value.otherTaxRate = data.otherTaxRate;
            quotesPresaleInfo.value.otherProfitRate = data.otherProfitRate;
            quotesPresaleInfo.value.xxOtherQuote = data.xxOtherQuote;
            quotesPresaleInfo.value.otherSugQuote = data.otherSugQuote;
            quotesPresaleInfo.value.otherExtQuote = data.otherExtQuote;

            quotesPresaleInfo.value.prePrice = data.prePrice;
            quotesPresaleInfo.value.preTaxRate = data.preTaxRate;
            quotesPresaleInfo.value.preProfitRate = data.preProfitRate;
            quotesPresaleInfo.value.xxPreQuote = data.xxPreQuote;
            quotesPresaleInfo.value.preSugQuote = data.preSugQuote;
            quotesPresaleInfo.value.preExtQuote = data.preExtQuote;

            quotesPresaleInfo.value.prodPrice = data.prodPrice;
            quotesPresaleInfo.value.prodTaxRate = data.prodTaxRate;
            quotesPresaleInfo.value.prodProfitRate = data.prodProfitRate;
            quotesPresaleInfo.value.xxProdQuote = data.xxProdQuote;
            quotesPresaleInfo.value.prodSugQuote = data.prodSugQuote;
            quotesPresaleInfo.value.prodExtQuote = data.prodExtQuote;

            quotesPresaleInfo.value.selfPrice = data.selfPrice;
            quotesPresaleInfo.value.selfTaxRate = data.selfTaxRate;
            quotesPresaleInfo.value.selfProfitRate = data.selfProfitRate;
            quotesPresaleInfo.value.xxSelfQuote = data.xxSelfQuote;
            quotesPresaleInfo.value.selfSugQuote = data.selfSugQuote;
            quotesPresaleInfo.value.selfExtQuote = data.selfExtQuote;

            quotesPresaleInfo.value.xxExtProxyQuote = data.xxExtProxyQuote

            quotesPresaleInfo.value.xxAdditionalQuote = data.xxAdditionalQuote


            // 小计数据  
            quotesPresaleInfo.value.softWareCostTotal = data.softWareCostTotal;
            quotesPresaleInfo.value.hardWareCostTotal = data.hardWareCostTotal;


            // 合计数据  
            quotesPresaleInfo.value.projCostTotal = data.projCostTotal;
            quotesPresaleInfo.value.projSugQuoteTotal = data.projSugQuoteTotal;
            quotesPresaleInfo.value.projProfitRateExcl = data.projProfitRateExcl;
            quotesPresaleInfo.value.projProfitRateIncl = data.projProfitRateIncl;

            //销售粗略报价
            description.value = data.description
            unableQuoteAmount.value = data.unableQuoteAmount
            signType.value = data.signType
            xxUnableQuoteAmount.value = data.xxUnableQuoteAmount
        } else {
            console.error('获取信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    } finally {
        loading.value = false;
    }
};
// 切换radiobutton
const handleRadioChange = () => {
    xxUnableQuoteAmount.value = 0
    unableQuoteAmount.value = 0
    if (props.isUpdateSalesOffer) {
        if (props.opportunity.type === 'INCAPABLE') {
            quoteType.value = 'upload';
        }
        getSalesInfoFunc(fetchReSalesInfo)
    } else {
        // 销售报价调取接口获取成本数据 根据成本数据 销售报价  
        if (props.opportunity.type === 'COST') {
            getSalesInfoFunc(fetchSalesDetailInfo);
        } else if (props.opportunity.type === 'ROUGH') {
            getSalesInfoFunc(fetchSalesRoughInfo);
        } else if (props.opportunity.type === 'INCAPABLE') {
            quoteType.value = 'upload';
            loading.value = false
        }
    }
}
const emit = defineEmits(['close', 'confirmQuote', 'handleLose', 'handleReapply']);
const isSaleQuoteModelVisible = ref(false)
// 上传相关
// const fileList = ref([]);
// const uploadedFiles = ref([]);

// const handleChange = async (info) => {
//     let newFileList = [...info.fileList];

//     newFileList = newFileList.filter(file => !uploadedFiles.value.some(uploadedFile => uploadedFile.name === file.name));
//     fileList.value = newFileList;

//     if (info.file.status !== 'uploading') {
//         console.log(info.file, fileList.value);
//     }

//     if (info.file.status === 'done') {
//         message.success(`${info.file.name} file uploaded successfully`);
//         // Add uploaded file to the uploadedFiles list
//         uploadedFiles.value.push(info.file);
//     } else if (info.file.status === 'error') {
//         message.error(`${info.file.name} file upload failed.`);
//     }
// };

// Upload files function
// const uploadFiles = async () => {
//     try {
//         if (fileList.value.length === 0) {
//             return;
//         }
//         console.log('formdata', fileList.value.map(file => file.originFileObj))
//         const response = await fetchUploads(fileList.value.map(file => file.originFileObj));
//         if (response && response.code === 200) {
//             message.success('Files uploaded successfully');
//             uploadedFiles.value = response.data;
//             fileList.value = [];
//         } else {
//             message.error('File upload failed: ' + response.message);
//         }
//     } catch (error) {
//         console.error("File upload request failed:", error);
//         message.error('File upload request failed');
//     }
// };

const handleQuoteOk = () => {
    if (signType.value === '1') {
        if (quotesPresaleInfo.value.xxAdditionalQuote == null) {
            message.info('其他费用欣象对外报价不能为空，请填写0或者大于0的数值');
            return;
        }
    }

    // 定义一个用于处理请求成功的函数  
    const handleSuccess = (response) => {
        console.log('保存成功:', response);
        // 可以根据需要更新状态或提示用户  
        // uploadFiles()
        emit('close');
    };

    // 定义一个用于处理请求失败的函数  
    const handleError = (error) => {
        console.error('保存失败:', error);
        // 提示用户保存失败的消息  
    };

    // 准备参数  
    const prepareParams = () => {
        if (quoteType.value === 'cost') {

            quotesPresaleInfo.value.projExtQuoteTotal = Number(projExtQuoteTotal.value)
            return quotesPresaleInfo.value;
        } else if (quoteType.value === 'upload') {
            return {
                opportunitiesId: props.opportunity.id,
                signType: signType.value,
                unableQuoteAmount: unableQuoteAmount.value, // 报价金额  
                xxUnableQuoteAmount: xxUnableQuoteAmount.value,
                description: description.value,// 情况说明  
                unableQuote: true
            };
        }
        return null;
    };

    // 处理报价  
    const params = prepareParams();

    if (quoteType.value === 'cost') {
        if (quotesPresaleInfo.value.projProfitRateExcl < externalProfit.value) {
            isDocumentaryModellVisible.value = true;
            return; // 早返回，避免后续代码执行  
        }
    }

    // 执行请求  
    if (params) {
        confirmSalesQuote(params)
            .then(handleSuccess)
            .catch(handleError)
    }
};

const confirmQuote = (closeNote) => {
    quotesPresaleInfo.value.closeNote = closeNote
    const params = quotesPresaleInfo.value
    confirmSalesQuote(params)
        .then(response => {
            // 处理成功的响应  
            console.log('保存成功:', response);
            // 可以根据需要更新状态或提示用户  
            emit('close');

        })
        .catch(error => {
            // 处理请求错误  
            console.error('保存失败:', error);
            // 提示用户保存失败的消息
        });
}
const handleCancel = () => {
    quoteType.value = 'cost'
    loading.value = true;
    isSaleQuoteModelVisible.value = false
    emit('close');

}
const handleDocumentClose = (param) => {
    isDocumentaryModellVisible.value = false
    if (param) {
        emit('close');
    }
};
function formatNum(value) {
    const parsedValue = parseFloat(value);
    if (isNaN(parsedValue)) {
        return 0;  // 如果无法转换为有效数字，返回'-'
    }
    return (parsedValue);
}
function formatPercentage(value) {
    const parsedValue = parseFloat(value);
    if (isNaN(parsedValue)) {
        return '-';  // 如果无法转换为有效数字，返回'-'
    }
    return (parsedValue * 100).toFixed(2) + '%';
}
const handleReapply = () => {
    quoteType.value = 'cost'
    emit('handleReapply', props.opportunity);
};
const handleLose = () => {
    quoteType.value = 'cost'
    emit('handleLose', props.opportunity);
};
</script>

<style scoped>
.highlight-row>>>.ant-table-cell {
    background-color: #3f4f6b !important;
    z-index: 999;
}

.section>>>.ant-input-number-input {
    text-align: center;
}

.modal-content {
    display: flex;
    flex-wrap: wrap;
    padding: 20px 20px 0 20px;
}

.modal-item {
    width: 33.33%;
    /* 更新为一行显示三个项 */
    margin-bottom: 10px;
    box-sizing: border-box;
    display: flex;
    align-items: flex-start;
}

.modal-item span {
    display: inline-block;
}

.modal-item span:first-child {
    white-space: nowrap;
}

.upload-btn {
    padding: 0 20px;
}

.section {
    /* margin-bottom: 20px; */
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    border: 1px solid #f0f0f0;
    padding: 8px;
    text-align: center;
}

th {
    background-color: #fafafa;
}

td[colspan="3"] {
    text-align: right;
    font-weight: bold;
}

.info p {
    margin: 5px 0;
}

.add-btn {
    margin-top: 20px;
}

.total {
    background-color: #f1f1f1;
    font-weight: 600;
    /* td {
        border: 1px solid #fff;
    } */
}

/* 预警 */
.highlight {
    color: red;
    /* 改为红色 */
    font-weight: bold;
    /* 加粗 */
    background-color: #ffcccc;
    /* 可选：背景高亮 */
}

.profit-rate-column {
    width: 70px;
}

.profit-quote-column {
    width: 120px;
}

/* .ant-radio-group {
    display: flex;
    width: 100%;
} */

/* a-radio {
    flex: 1;
    text-align: center;
} */
.nlQuoteStyle {
    margin-inline: 10px;
    font-size: 14px;
    text-align: center;
    display: flex;
    align-items: center;
}

.disabled-text {
    color: gray;
}

.disabled-text>>>.ant-input-number-input {
    color: gray;

}

.table-title {
    font-size: 18px;
    line-height: 90px;
    font-weight: 700;
}

.first-title {
    margin-top: -20px !important;
    display: block;
}
</style>