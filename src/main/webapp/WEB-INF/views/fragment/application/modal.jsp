<!-- Add application form -->
<div
    id="addFormContainer"
    class="modal hide fade"
    tabindex="-1"
    role="dialog"
    aria-labelledby="myModalLabel"
    aria-hidden="true">
    <div class="modal-header">
        <h3>Add application</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal">
            <div class="control-group">
                <label
                    class="control-label"
                    for="name">Name</label>
                <div class="controls">
                    <input
                        type="text"
                        id="name"
                        name="name"
                        placeholder="Enter application name here">
                    </input>
                </div>
            </div>
            <div class="control-group">
                <label
                    class="control-label"
                    for="description">Description</label>
                <div class="controls">
                    <input
                        type="text"
                        id="description"
                        name="description"
                        placeholder="Enter description here">
                    </input>
                </div>
            </div>
        </form>
        <div class="modal-footer">
            <button
                id="modalSubmit"
                class="btn btn-primary">Save changes</button>
            <button
                class="btn"
                data-dismiss="modal">Close</button>
        </div>
    </div>

</div>
<!-- Edit application form -->
<div
    id="myModal"
    class="modal hide fade"
    tabindex="-1"
    role="dialog"
    aria-labelledby="myModalLabel"
    aria-hidden="true">
    <div class="modal-header">
        <h3>Edit application</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal">
            <div class="control-group">
                <label
                    class="control-label"
                    for="name">Name</label>
                <div class="controls">
                    <input
                        type="text"
                        id="name"
                        name="name"
                        placeholder="Enter application name here">
                    </input>
                </div>
            </div>
            <div class="control-group">
                <label
                    class="control-label"
                    for="description">Description</label>
                <div class="controls">
                    <input
                        type="text"
                        id="description"
                        name="description"
                        placeholder="Enter description here">
                    </input>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button
            id="modalSubmit"
            class="btn btn-primary">Save changes</button>
        <button
            class="btn"
            data-dismiss="modal">Close</button>
    </div>
</div>
<!-- Preview modal form -->
<div
    id="applicationPreviewModal"
    class="modal hide fade"
    tabindex="-1"
    role="dialog"
    aria-labelledby="myModalLabel"
    aria-hidden="true">
    <div class="modal-header">
        <h2>Application preview</h2>
    </div>
    <div class="modal-body">
        <form class="form-horizontal">
            <div class="control-group">
                <label
                    class="control-label"
                    for="name">Name</label>
                <div class="controls">
                    <input
                        type="text"
                        id="name"
                        name="name"
                        disabled>
                    </input>
                </div>
            </div>
            <div class="control-group">
                <label
                    class="control-label"
                    for="description">Description</label>
                <div class="controls">
                    <input
                        type="text"
                        id="description"
                        name="description"
                        disabled>
                    </input>
                </div>
            </div>
            <div class="control-group">
                <label
                    class="control-label"
                    for="apiKey">Api key</label>
                <div class="controls">
                    <input
                        type="text"
                        id="description"
                        name="apiKey"
                        disabled>
                    </input>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button
            class="btn"
            data-dismiss="modal">Close</button>
    </div>
</div>